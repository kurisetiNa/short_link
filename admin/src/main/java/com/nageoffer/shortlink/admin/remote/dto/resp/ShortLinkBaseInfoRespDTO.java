package com.nageoffer.shortlink.admin.remote.dto.resp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkBaseInfoRespDTO {

    @ExcelProperty("标题")
    @ColumnWidth(40)
    private String describe;

    @ExcelProperty("短链接")
    @ColumnWidth(40)
    private String fullShortUrl;

    @ExcelProperty("原始链接")
    @ColumnWidth(80)
    private String originUrl;
}
