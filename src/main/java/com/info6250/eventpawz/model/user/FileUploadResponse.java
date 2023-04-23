package com.info6250.eventpawz.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadResponse {
    private String fileName;
    private String filePath;
    private long size;
}
