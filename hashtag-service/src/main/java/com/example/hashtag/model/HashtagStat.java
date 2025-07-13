package com.example.hashtag.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HashtagStat {
    private String hashtag;
    private int count;
}