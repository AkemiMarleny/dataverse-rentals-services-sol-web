package com.dataverse.dataverserentals.common.helpers;

import org.springframework.util.DigestUtils;

public class Checksum {
    private final String sum;

    public Checksum(String content) {
        this.sum = DigestUtils.md5DigestAsHex(content.getBytes());
    }

    public String get() {
        return this.sum;
    }
}