package com.tuotiansudai.fudian.ump.sync.response;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.util.AmountUtils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class TranseqSearchResponseModel extends BaseSyncResponseModel {

    private static final Map<String, String> HUMAN_READABLE_TRANS_STATUS = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("01", "成功")
            .put("02", "冲正")
            .put("99", "其他")
            .build());

    private static final Map<String, String> HUMAN_READABLE_TRANS_TYPE = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("01", "充值")
            .put("02", "提现")
            .put("03", "标的转账")
            .put("04", "转账")
            .put("05", "提现失败退回")
            .put("99", "退费等其他交易")
            .build());

    private String totalNum;

    private String transDetail;

    @Override
    public void initializeModel(Map<String, String> resData) {
        super.initializeModel(resData);
        this.totalNum = resData.get("total_num");
        this.transDetail = resData.get("trans_detail");
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getTransDetail() {
        return transDetail;
    }

    public void setTransDetail(String transDetail) {
        this.transDetail = transDetail;
    }

    public List<List<String>> generateHumanReadableData() throws ParseException {
        List<List<String>> humanReadableData = Lists.newArrayList();

        if (Integer.parseInt(this.totalNum) > 0) {
            List<String> rawData = Lists.newArrayList(this.transDetail.split("\\|"));
            List<Map<String, String>> formatRowData = Lists.newArrayList();
            for (String row : rawData) {
                Map<String, String> map = Maps.newHashMap();
                formatRowData.add(map);
                List<String> keyValues = Lists.newArrayList(row.split(","));
                for (String keyValue : keyValues) {
                    String[] split = keyValue.split("=");
                    map.put(split[0], split[1]);
                }
            }

            for (Map<String, String> values : formatRowData) {
                List<String> row = Lists.newArrayList();
                row.add(values.get("order_id"));
                row.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new SimpleDateFormat("yyyyMMddHHmmss").parse(values.get("trans_date") + values.get("trans_time"))));
                row.add(MessageFormat.format("{0}{1}",
                        values.get("dc_mark").equals("01") ? "+" : "-",
                        AmountUtils.toYuan(Long.parseLong(values.get("amount")))));
                row.add(AmountUtils.toYuan(Long.parseLong(values.get("balance"))));
                row.add(HUMAN_READABLE_TRANS_STATUS.get(values.get("trans_state")));
                row.add(HUMAN_READABLE_TRANS_TYPE.get(values.get("trans_type")));
                humanReadableData.add(row);
            }
        }

        return humanReadableData;
    }
}
