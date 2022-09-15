package io.github.cosminseceleanu.tutorials.sampleapp.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder(toBuilder = true)
public class User {
    String id;
    String email;
    String name;
}
