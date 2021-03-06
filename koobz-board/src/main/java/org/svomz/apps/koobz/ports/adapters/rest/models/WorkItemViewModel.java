package org.svomz.apps.koobz.ports.adapters.rest.models;


import org.svomz.apps.koobz.domain.model.WorkItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

public class WorkItemViewModel {

  private WorkItem workItem;

  public WorkItemViewModel(final WorkItem workItem) {
    Preconditions.checkNotNull(workItem);
    
    this.workItem = workItem;
  }
  
  @JsonProperty("id")
  public String getId() {
    return this.workItem.getId();
  }
  
  @JsonProperty("title")
  public String getTitle() {
    return this.workItem.getTitle();
  }
  
  @JsonProperty("order")
  public int getOrder() {
    return this.workItem.getPriority();
  }

  @JsonProperty("description")
  public String getDescription() {
    return this.workItem.getDescription();
  }
}
