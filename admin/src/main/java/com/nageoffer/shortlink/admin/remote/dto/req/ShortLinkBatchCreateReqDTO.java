package com.nageoffer.shortlink.admin.remote.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShortLinkBatchCreateReqDTO {

    private List<String> originUrls;
    private List<String> describes;
    private String gid;
    private Integer createdType;
    private Integer validDateType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime validDate;
}
