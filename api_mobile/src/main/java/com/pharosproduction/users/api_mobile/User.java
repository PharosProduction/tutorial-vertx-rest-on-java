package com.pharosproduction.users.api_mobile;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
class User {

  // Variables

  private String mFirstName;
  private String mLastName;

  // Constructors

  User(String firstName, String lastName) {
    mFirstName = firstName;
    mLastName = lastName;
  }

  // Accessors

  @JsonProperty
  String getFirstName() {
    return mFirstName;
  }

  @JsonProperty
  String getLastName() {
    return mLastName;
  }
}

