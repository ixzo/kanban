package org.svomz.apps.koobz.ports.adapters.rest.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import org.svomz.apps.koobz.domain.model.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 12/12/15.
 */
public class StageViewModel {

  private final Stage stage;

  public StageViewModel(Stage stage) {
    this.stage = stage;
  }

  @JsonProperty("id")
  public String getId() {
    return this.stage.getId();
  }

  @JsonProperty("name")
  public String getName() {
    return this.stage.getName();
  }

  @JsonProperty("order")
  public int getOrder() {
    return this.stage.getPosition();
  }

  @JsonProperty("workItems")
  @JsonView(WorkflowViewModel.FullView.class)
  public List<WorkItemViewModel> getWorkItems() {
    List<WorkItemViewModel> models = new ArrayList<>();

    this.stage.getWorkItems().forEach(workItem -> {
      models.add(new WorkItemViewModel(workItem));
    });

    return models;
  }
}
