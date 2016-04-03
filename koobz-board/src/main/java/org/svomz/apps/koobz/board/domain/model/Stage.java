package org.svomz.apps.koobz.board.domain.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.base.Preconditions;


@Entity
@Table(name = "stages")
public class Stage {

  @Id
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "position")
  private int order;

  @OneToMany(mappedBy = "stage", fetch = FetchType.EAGER)
  private Set<WorkItem> workItems;
  
  @ManyToOne
  @JoinColumn(name = "board_id")
  private Board board;

  /** Needed by JPA */
  private Stage() {}

  Stage(final String stageId, final String name) {
    this.id = Preconditions.checkNotNull(stageId);
    this.setName(name);
    this.workItems = new HashSet<>();
  }

  public void setName(String name) {
    this.name = Preconditions.checkNotNull(name);
  }

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  /**
   * @return the list of work items on the board. This list does not contains archived items.
   */
  public Set<WorkItem> getWorkItems() {
    Set<WorkItem>
      notArchivedWorkItems = this.workItems.stream()
      .filter(workItem -> !workItem.isArchived())
      .collect(Collectors.toSet());

    return Collections.unmodifiableSet(notArchivedWorkItems);
  }

  Stage addWorkItem(final WorkItem workItem) {
    Preconditions.checkNotNull(workItem, "The given workItem must not be null.");

    workItem.setStage(this);
    workItem.setPosition(this.workItems.size());
    this.workItems.add(workItem);
    return this;
  }

  Stage removeWorkItem(final WorkItem workItem) {
    Preconditions.checkNotNull(workItem, "The given workItem must not be null.");

    this.workItems.remove(workItem);
    int workItemOrder = workItem.getPosition();

    //reoder all items after the removed one
    this.workItems.forEach(item -> {
      if (item.getPosition() > workItemOrder) {
        int currentOrder = item.getPosition();
        item.setPosition(currentOrder - 1);
      }
    });

    return this;
  }
  
  Stage setBoard(final Board board) {
    Preconditions.checkNotNull(board, "The given board must not be null.");
    
    this.board = board;
    return this;
  }
  
  Stage putWorkItemAtPosition(WorkItem workItem, int position) throws WorkItemNotInStageException {
    Preconditions.checkNotNull(workItem);
    Preconditions.checkArgument(position >= 0);
    
    if (!this.workItems.contains(workItem)) {
      throw new WorkItemNotInStageException();
    }
    
    if (position == workItem.getPosition()) {
      return this;
    }
    
    if (position > this.workItems.size()) {
      position = this.workItems.size() - 1;
    }
    
    if (position > workItem.getPosition()) {
      for (WorkItem item : this.workItems) {
        if (item.getPosition() > workItem.getPosition() && item.getPosition() <= position) {
          item.setPosition(item.getPosition() - 1);
        }
      }
    } else {
      for (WorkItem item : this.workItems) {
        if (item.getPosition() >= position && item.getPosition() < workItem.getPosition()) {
          item.setPosition(item.getPosition() + 1);
        }
      }
    }
    
    workItem.setPosition(position);
    return this;
  }

  void setPosition(int order) {
    this.order = order;
  }

  public int getPosition() {
    return this.order;
  }

  boolean hasWorkItems() {
    return this.getWorkItems().size() > 0;
  }

}
