OLD_DB_HOST=192.168.100.77
DB_PORT=3306
DB_USER=root
#NEW_DB_HOST=192.168.1.249
NEW_DB_HOST=192.168.100.77

DB_CON_OLD="mysql -h$OLD_DB_HOST -P$DB_PORT -u$DB_USER tuotiansudai "
DB_CON_NEW="mysql -h$NEW_DB_HOST -P$DB_PORT -u$DB_USER aa "

SCRIPT_PATH=scripts/data_migration/migrate.py
LOG_PATH=/tmp/migrate

BATCH_SIZE=30000
BATCH_SIZE_user=15000
BATCH_SIZE_system_bill=15000
BATCH_SIZE_withdraw=10000

echo ""
echo "###### CREATE user_bill_seq_temp TABLE FOR account "
`$DB_CON_OLD -e"DROP TABLE IF EXISTS user_bill_seq_temp; CREATE TABLE user_bill_seq_temp (index ix_user_id (user_id)) as select user_id, max(seq_num) as max_seq from user_bill group by user_id;"`

echo ""
echo "###### ADD INDEX ON seq_num FIELD IN user_bill "
`$DB_CON_OLD -e"ALTER TABLE user_bill add index ix_seq_num(seq_num)"`

echo ""
echo "####### DELETE WRONG DATA IN bank_card "
`$DB_CON_OLD -e"delete from bank_card where user_id = 'susiepo' and card_no = '6212261203009214445';delete from bank_card where user_id = 'zjh1036517331' and card_no = '6228480089814033877';"`

echo ""
echo "####### CREATE bank_card_temp TABLE "
python scripts/data_migration/bank_card_pre.py

echo ""
echo "####### CREATE withdraw_cash_temp TABLE "
python scripts/data_migration/withdraw_pre.py


echo ""
echo "###### Search old data count, wait some seconds #####"
COUNT_account=`$DB_CON_OLD -e"select count(1) from trusteeship_account" | sed -n 2p`
COUNT_audit_log=`$DB_CON_OLD -e"select count(1) from user_info_log where is_success=1" | sed -n 2p`
COUNT_announce=`$DB_CON_OLD -e"select count(1) from node n left join node_body nb on n.body=nb.id where n.node_type='article'" | sed -n 2p`
COUNT_bank_card=`$DB_CON_OLD -e"select count(1) from bank_card_temp" | sed -n 2p`
COUNT_loan=`$DB_CON_OLD -e"select count(1) from loan WHERE status NOT IN ('test', 'verify_fail') AND type != 'loan_type_2'" | sed -n 2p`
COUNT_loan_repay=`$DB_CON_OLD -e"select count(1) from loan_repay WHERE status <> 'test' and loan_id not in (select l.id from loan l where l.status in ('verify_fail','test') or l.type='loan_type_2')" | sed -n 2p`
COUNT_recharge=`$DB_CON_OLD -e"select count(1) from recharge" | sed -n 2p`
COUNT_system_bill=`$DB_CON_OLD -e"select count(1) FROM system_bill WHERE reason IN ('activity_reward', 'binding_card', 'invest_fee', 'replace_card', 'referrer_reward')" | sed -n 2p`
COUNT_user=`$DB_CON_OLD -e"select count(1) from user" | sed -n 2p`
COUNT_user_bill=`$DB_CON_OLD -e"SELECT count(1) FROM user_bill where detail not in ('未在联动优势开通账户,交易失败','未在联动优势绑定借记卡,交易失败')" | sed -n 2p`
COUNT_user_role=`$DB_CON_OLD -e"select count(1) FROM user_role WHERE user_role.role_id IN ('ADMINISTRATOR', 'custorm-service', 'INVESTOR', 'LOANER', 'MEMBER', 'ROLE_MERCHANDISER')" | sed -n 2p`
COUNT_withdraw=`$DB_CON_OLD -e"select count(1) FROM withdraw_cash where is_withdraw_by_admin is null" | sed -n 2p`
COUNT_invest=`$DB_CON_OLD -e"select count(1) FROM invest WHERE status <> 'test'" | sed -n 2p`
COUNT_invest_repay=`$DB_CON_OLD -e"select count(1) FROM invest_repay WHERE status <> 'test'" | sed -n 2p`
COUNT_invest_referrer_reward=`$DB_CON_OLD -e"select count(1) FROM invest_userreferrer where invest_id not in (select id from invest where status='test')" | sed -n 2p`
COUNT_referrer_relation=`$DB_CON_OLD -e"select sum(a) from (select count(1) as a from referrer_relation rr where not exists(select 1 from user_role ur where ur.user_id = rr.referrer_id and ur.role_id='ROLE_MERCHANDISER') and level<=2 union select count(1) as a from referrer_relation rr where exists(select 1 from user_role ur where ur.user_id = rr.referrer_id and ur.role_id='ROLE_MERCHANDISER') and level<=4) aa" | sed -n 2p`

#echo "###### Search old data count, DONE #####"


clear_table(){
  if [ -n "$1" ]; then
    tableName=$1
    echo ""
    echo "###### Clear $tableName table Start ######"
    $DB_CON_NEW -e "set foreign_key_checks=0; truncate table $tableName"
    #echo "###### Clear $tableName table Done ######"
  fi
}

clear_db(){
  echo ""
  echo "###### Clear aa DB Start ######"
  sleep 1s
  TABLES="user account announce referrer_relation audit_log bank_card loan loan_title_relation invest invest_referrer_reward invest_repay loan_repay recharge system_bill user_bill user_role withdraw"

  SQL='set foreign_key_checks=0;'
  for t in $TABLES; do SQL=$SQL'truncate table '$t';';done;
  #echo "SQL: $SQL";
  $DB_CON_NEW -e"$SQL";
  #echo "###### Clear aa DB Done ######"
}

simple_migrate(){
  tableName=$1
  echo ""
  echo "##### MIGRATE $tableName "
  {
    #echo "$tableName start";
    python $SCRIPT_PATH -t $tableName 2>$LOG_PATH/$tableName.log;
    #echo "$tableName end";
  }&
  wait $!
  echo ""
  echo "$tableName  DONE #####"
}

batch_migrate() {
  tableName=$1
  echo ""
  echo "##### MIGRATE $tableName "

  eval specialCount=$(echo \${BATCH_SIZE_$tableName})

  if [ ! -z $specialCount ] ; then
    #echo $tableName" special batch size:"$specialCount
    BATCH_SIZE=$specialCount
  fi

  eval total=$(echo \${COUNT_$tableName})
  loopNum=$[total/BATCH_SIZE]
  Pid="";

  for i in $(seq 0 $loopNum); do
  {
    #echo $tableName"_"$i" start";
    command="python $SCRIPT_PATH -t $tableName -s $[i*BATCH_SIZE] -c $BATCH_SIZE 2>$LOG_PATH/${tableName}_$i.log;"
    #echo $command
    eval $command
    #echo $tableName"_"$i" end";
  }&
  Pid=$Pid" "$!
  sleep 0.5;
  done;
  #echo $tableName"-Pid:"$Pid
  wait $Pid
  echo ""
  echo "$tableName  DONE #####"
}

simple_verify(){
  tableName=$1
  eval oldCount=$(echo \${COUNT_$tableName})
  newCount=`$DB_CON_NEW -e"select count(1) from $tableName" | sed -n 2p`
  if (( oldCount == newCount)); then
    echo "$tableName migrate success";
  else
    echo "$tableName migrate fail,oldCount:$oldCount,newCount:$newCount";
  fi
}


verify_system_bill(){
  oldCount=`$DB_CON_OLD -e"select count(distinct money, type, reason, time) from system_bill WHERE reason IN ('activity_reward', 'binding_card', 'invest_fee', 'replace_card', 'referrer_reward')" | sed -n 2p`
  newCount=`$DB_CON_NEW -e"select count(distinct amount, operation_type,business_type,created_time) from system_bill" | sed -n 2p`
  if (( oldCount == newCount)); then
    echo "system_bill migrate success";
  else
    echo "system_bill migrate fail,oldCount:$oldCount,newCount:$newCount";
  fi
}

verify_system_bill_amount(){
  oldAmount=`$DB_CON_OLD -e"select round(sum(money)*100,0) FROM system_bill WHERE reason IN ('activity_reward', 'binding_card', 'invest_fee', 'replace_card', 'referrer_reward')" | sed -n 2p`
  newAmount=`$DB_CON_NEW -e"select sum(amount) from system_bill" | sed -n 2p`
  if (( oldAmount == newAmount)); then
    echo "system_bill amount check passed";
  else
    echo "system_bill amount check fail,oldAmount:$oldAmount,newAmount:$newAmount";
  fi
}

verify_user_bill(){
  oldCount=`$DB_CON_OLD -e"select count(distinct user_id, money, balance, frozen_money, type, type_info, time) from user_bill where detail not in ('未在联动优势开通账户,交易失败','未在联动优势绑定借记卡,交易失败')" | sed -n 2p`
  newCount=`$DB_CON_NEW -e"select count(distinct login_name, amount, balance, freeze, operation_type, business_type, created_time) from user_bill" | sed -n 2p`
  if (( oldCount == newCount)); then
    echo "user_bill migrate success";
  else
    echo "user_bill migrate fail,oldCount:$oldCount,newCount:$newCount";
  fi
}

verify_user_bill_amount(){
  oldAmount=`$DB_CON_OLD -e"select round(sum(money)*100,0) FROM user_bill where detail not in ('未在联动优势开通账户,交易失败','未在联动优势绑定借记卡,交易失败')" | sed -n 2p`
  newAmount=`$DB_CON_NEW -e"select sum(amount) from user_bill" | sed -n 2p`
  if (( oldAmount == newAmount)); then
    echo "user_bill amount check passed";
  else
    echo "user_bill amount check fail,oldAmount:$oldAmount,newAmount:$newAmount";
  fi
}

verify_recharge(){
  oldCount=`$DB_CON_OLD -e"select count(distinct user_id, actual_money, fee, recharge_way, status, source, time, channel) FROM recharge" | sed -n 2p`
  newCount=`$DB_CON_NEW -e"select count(distinct login_name, amount, fee, bank_code, status, source, created_time, channel) from recharge" | sed -n 2p`
  if (( oldCount == newCount)); then
    echo "recharge migrate success";
  else
    echo "recharge migrate fail,oldCount:$oldCount,newCount:$newCount";
  fi
}

verify_recharge_amount(){
  oldAmount=`$DB_CON_OLD -e"select round(sum(actual_money)*100,0) FROM recharge" | sed -n 2p`
  newAmount=`$DB_CON_NEW -e"select sum(amount) from recharge" | sed -n 2p`
  if (( oldAmount == newAmount)); then
    echo "recharge amount check passed";
  else
    echo "recharge amount check fail,oldAmount:$oldAmount,newAmount:$newAmount";
  fi
}

verify_withdraw_amount(){
  oldAmount=`$DB_CON_OLD -e"select round(sum(money)*100,0) from withdraw_cash where is_withdraw_by_admin IS NULL;" | sed -n 2p`
  newAmount=`$DB_CON_NEW -e"select sum(amount) from withdraw" | sed -n 2p`
  if (( oldAmount == newAmount)); then
    echo "withdraw amount check pass";
  else
    echo "withdraw amount check fail,oldAmount:$oldAmount,newAmount:$newAmount";
  fi
}


migrate_all(){
echo ""
echo "##### migrate v1 tables into new db #####"

  simple_migrate announce &
  batch_migrate user &
  user_pid=$!

  batch_migrate system_bill &

  wait $user_pid
  batch_migrate account &
  sleep 10

  batch_migrate user_role &
  batch_migrate user_bill &
#  wait
  simple_migrate audit_log &
  batch_migrate referrer_relation &

  batch_migrate recharge &

{
  batch_migrate bank_card &
  bank_card_pid=$!

  wait $bank_card_pid
  batch_migrate withdraw
}&

  simple_migrate loan &
  loan_pid=$!

  wait $loan_pid
  simple_migrate loan_title_relation &
  simple_migrate loan_repay &

  simple_migrate invest &
  invest_pid=$!

  wait $invest_pid
  simple_migrate invest_repay &
  simple_migrate invest_referrer_reward &
  wait
}


verify(){
  echo ""
  echo ""
  echo "####### Start Verify #######"

  simple_verify user
  simple_verify announce
  simple_verify audit_log
  simple_verify account
  simple_verify referrer_relation
  simple_verify loan
  simple_verify loan_repay
  simple_verify invest
  simple_verify invest_repay
  simple_verify invest_referrer_reward
  simple_verify user_role

  simple_verify bank_card
  simple_verify withdraw
  verify_withdraw_amount

  verify_system_bill
  verify_system_bill_amount

  verify_user_bill
  verify_user_bill_amount

  verify_recharge
  verify_recharge_amount
  echo "####### Verify Done #######"
}

main(){
  if [ ! -n "$1" ] ; then
#    old_data_count
    clear_db
    migrate_all
    wait
    verify
  else
    tableName=$1
    clear_table $tableName
    batch_migrate $tableName
    wait
    simple_verify $tableName
  fi
}

main $1

echo ""
echo "####### DROP TABLE IF EXISTS user_bill_seq_temp "
`$DB_CON_OLD -e"DROP TABLE IF EXISTS user_bill_seq_temp;"`

echo ""
echo "####### SET AUTO_INCREMENT IN user_bill TABLE"
`$DB_CON_NEW -e"ALTER TABLE user_bill AUTO_INCREMENT=$[$COUNT_user_bill+1];"`

echo ""
echo "####### DROP TABLE bank_card_temp "
`$DB_CON_OLD -e"DROP TABLE IF EXISTS bank_card_temp;"`

echo ""
echo "####### DROP TABLE withdraw_cash_temp "
`$DB_CON_OLD -e"DROP TABLE IF EXISTS withdraw_cash_temp;"`

: '
clear_table user
batch_migrate user &
user_pid=$!
echo "user_pid:"$user_pid
wait $user_pid
echo "done"
'
