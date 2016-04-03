package org.svomz.apps.koobz.board.application;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BoardIdentityService {

  public String nextStageIdentity() {
    return UUID.randomUUID().toString();
  }

  public String nextBoardIdentity() {
    return UUID.randomUUID().toString();
  }
}
