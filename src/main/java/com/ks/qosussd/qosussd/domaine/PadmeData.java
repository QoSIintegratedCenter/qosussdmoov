package com.ks.qosussd.qosussd.domaine;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PadmeData implements Serializable {

    private String codUsuario;
    private String saldoCuenta;
    private String codCuenta;
    private Date fechaPro;
}
