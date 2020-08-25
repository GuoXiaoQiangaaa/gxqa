package com.pwc.modules.filing.controller.form;

import com.pwc.modules.filing.entity.FilingDistrictEntity;
import lombok.Data;

import java.util.List;

/**
 * @author zk
 */
@Data
public class FilingDistrictForm {
        List<FilingDistrictEntity> districts;
}
