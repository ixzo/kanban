package org.svomz.apps.kanban.application.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

public class BoardInputModel {

  @NotNull
  @Size(min = 1, max = 255)
  private final String name;

  @JsonCreator
  public BoardInputModel(@JsonProperty("name") final String name) {
    Preconditions.checkNotNull(name);

    this.name = name;
  }

  public String getName() {
    return name;
  }

}
