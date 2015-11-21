from scripts.data_migration.base import BaseMigrate

class LoanMigrate(BaseMigrate):
    """
    Class Naming Convention: `NewTableNameMigrate(BaseMigrate)`
    """
    # select sql which is executed on original db (edxapp, tuotiansudai etc)
    SELECT_SQL = "SELECT
                    @rownum := @rownum + 1 AS id,
                    old_loan.*
                  FROM
                    (SELECT
                      @rownum := 0) r,
                    (SELECT
                      loan.name,
                      loan.agent AS agent_login_name,
                      loan.user_id AS loaner_login_name,
                      CASE
                        WHEN loan.type = 'loan_type_1'
                        THEN 'INVEST_INTEREST_MONTHLY_REPAY'
                        WHEN loan.type = 'loan_type_3'
                        THEN 'LOAN_INTEREST_MONTHLY_REPAY'
                        WHEN loan.type = 'loan_type_4'
                        THEN 'LOAN_INTEREST_LUMP_SUM_REPAY'
                        WHEN loan.type = 'loan_type_5'
                        THEN 'INVEST_INTEREST_LUMP_SUM_REPAY'
                      END AS TYPE,
                      loan.deadline AS periods,
                      loan.description AS description_html,
                      ROUND(loan.loan_money * 100) AS loan_money,
                      IFNULL(loan.investor_fee_rate, 0) AS invest_fee_rate,
                      ROUND(loan.min_invest_money * 100) AS min_invest_money,
                      ROUND(loan.max_invest_money * 100) AS max_invest_money,
                      ROUND(loan.cardinal_number * 100) AS invest_increasing_amount,
                      CASE
                        WHEN loan.loan_activity_type = 'pt'
                        THEN 'NORMAL'
                        WHEN loan.loan_activity_type = 'xs'
                        THEN 'NEWBIE'
                        WHEN loan.loan_activity_type = 'jx'
                        THEN 'PROMOTION'
                        WHEN loan.loan_activity_type = 'dx'
                        THEN 'EXCLUSIVE'
                      END AS activity_type,
                      loan.jk_rate AS base_rate,
                      loan.hd_rate AS activity_rate,
                      789098123 AS contract_id,
                      loan.invest_begin_time AS fundraising_start_time,
                      loan.expect_time AS fundraising_end_time,
                      loan.verify_time AS raising_complete_time,
                      loan.verify_time,
                      loan.give_money_time AS recheck_time,
                      CASE
                        WHEN loan.status = 'waiting_verify'
                        THEN 'WAITING_VERIFY'
                        WHEN loan.status = 'raising'
                        THEN 'RAISING'
                        WHEN loan.status = 'recheck'
                        THEN 'RECHECK'
                        WHEN loan.status = 'cancel'
                        THEN 'CANCEL'
                        WHEN loan.status = 'repaying'
                        THEN 'REPAYING'
                        WHEN loan.status = 'overdue'
                        THEN 'OVERDUE'
                        WHEN loan.status = 'complete'
                        THEN 'COMPLETE'
                      END AS STATUS,
                      IF(
                        loan_node_attr.node_attr_id = 'index',
                        TRUE,
                        FALSE
                      ) AS show_on_home,
                      loan.commit_time AS created_time,
                      loan.commit_time AS update_time,
                      loan.id AS old_loan_id
                    FROM
                      loan
                      LEFT JOIN loan_node_attr
                        ON loan.id = loan_node_attr.loan_id) old_loan "
    # insert sql which is executed on aa db
    INSERT_SQL = "INSERT INTO loan 