package org.svomz.apps.koobz.board.application;

import com.google.common.base.Preconditions;

import org.springframework.stereotype.Service;
import org.svomz.apps.koobz.board.domain.model.Board;
import org.svomz.apps.koobz.board.domain.model.BoardRepository;
import org.svomz.apps.koobz.board.domain.model.Stage;
import org.svomz.apps.koobz.board.domain.model.StageNotInProcessException;
import org.svomz.apps.koobz.board.domain.model.WorkItem;


import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Service
public class BoardApplicationService {

  private final BoardRepository boardRepository;

  @Inject
  public BoardApplicationService(final BoardRepository aBoardRepository) {
    this.boardRepository = aBoardRepository;
  }

  @Transactional
  public Board createBoard(final String aBoardName) {
    Preconditions.checkNotNull(aBoardName);

    Board board = new Board(
      this.boardRepository().nextIdentity(),
      aBoardName
    );
    this.boardRepository().save(board);
    return board;
  }

  @Transactional
  public Board findBoard(final String aBoardId) throws BoardNotFoundException {
    Preconditions.checkNotNull(aBoardId);

    Board board = this.boardRepository().findOne(aBoardId);
    if (board == null) {
      throw new BoardNotFoundException(aBoardId);
    }
    return board;
  }

  @Transactional
  public Stage createStage(final String boardId, final String title) throws BoardNotFoundException {
    Preconditions.checkNotNull(boardId);
    Preconditions.checkNotNull(title);

    Board board = this.findBoard(boardId);
    Stage stage = new Stage(title);

    stage.addToBoard(board);

    return stage;
  }

  @Transactional
  public WorkItem createWorkItem(final String boardId, final String stageId, final String aWorkItemTitle,
    final String aWorkItemDescription) throws BoardNotFoundException, StageNotInProcessException {

    Board board = this.findBoard(boardId);

    Optional<Stage> stage = board.getStage(stageId);
    if (!stage.isPresent()) {
      throw new StageNotInProcessException();
    }

    WorkItem workItem = new WorkItem(aWorkItemTitle, aWorkItemDescription);
    board.addWorkItem(workItem, stage.get());
    return workItem;
  }

  private BoardRepository boardRepository() {
    return boardRepository;
  }
}