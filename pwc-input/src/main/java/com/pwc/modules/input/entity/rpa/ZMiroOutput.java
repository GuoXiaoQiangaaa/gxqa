package com.pwc.modules.input.entity.rpa;

import java.util.ArrayList;
import java.util.List;

public class ZMiroOutput {
    public String INV_DOC_NO;  //发票凭证的凭证编号
    public String FISCALYEAR;  //会计年度
    public String BELNR; //会计凭证编号
    public List<ZRFCReturn> listReturns = new ArrayList<ZRFCReturn>();

    @Override
    public String toString() {
        return "ZMiroOutput{" +
                "INV_DOC_NO='" + INV_DOC_NO + '\'' +
                ", FISCALYEAR='" + FISCALYEAR + '\'' +
                ", BELNR='" + BELNR + '\'' +
                ", listReturns=" + listReturns +
                '}';
    }
}
