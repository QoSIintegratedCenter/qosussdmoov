package com.ks.qosussd.qosussd.domaine;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//import java.util.Map;
@Data
public class GetListObjet {
    private List<PadmeData> mapList;

    public GetListObjet() {
        mapList = new ArrayList<>();
    }

}
