package com.mx.academy_check.util;

import com.google.gson.GsonBuilder;

public class JsonConvertible {

  public String toJson() {
    return new GsonBuilder().setPrettyPrinting().create().toJson(this);
  }

  public JsonConvertible fromJson(String str) {
    if (str == null || str.length() == 0) {
      return this;
    }
    return new GsonBuilder().setPrettyPrinting().create().fromJson(str, this.getClass());
  }

  public JsonConvertible getClone() {
    return fromJson(toJson());
  }
}
