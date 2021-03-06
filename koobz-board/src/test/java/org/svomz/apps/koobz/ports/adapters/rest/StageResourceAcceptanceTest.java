package org.svomz.apps.koobz.ports.adapters.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.svomz.apps.koobz.ports.adapters.rest.models.StageInputModel;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;

public class StageResourceAcceptanceTest extends AbstractAcceptanceTest {
  
  /**
   * As an api user
   * Given a board exits
   * When 1 add a new stage to this board
   * Then I receive 201
   */
  @Test
  public void testCreateStage() {
    String workflowName = "Test1";
    JsonPath boardJsonPath = createWorkflow(workflowName);
    
    String boardId = boardJsonPath.get("id");
    createStage(boardId, "To do");
  }
  
  /**
   * As an api user
   * Given a board exists with a stage
   * When I update this stage
   * Then I receive the updated stage
   */
  @Test
  public void testUpdateStage() {
    String boardId = this.createWorkflow("Test 1").get("id");
    JsonPath json = this.createStage(boardId, "Stage 1");
    String stageNewName = "New name for stage 1";
    StageInputModel updateRequest = new StageInputModel(stageNewName);
    
    String stageId = json.get("id");
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(updateRequest)
    .when()
      .put("/workflows/" + boardId + "/stages/" + stageId)
    .then()
      .statusCode(200);
  }
  
  /**
   * As an api user
   * Given a stage exists without workitems
   * When I delete it
   * Then I receive 204
   */
  @Test
  public void testDeleteStageWithoutWorkItems() {
    String boardId = this.createWorkflow("Test 1").get("id");
    JsonPath json = this.createStage(boardId, "Stage 1");
    
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
    .when()
      .delete("/workflows/" + boardId + "/stages/" + json.get("id"))
    .then()
      .statusCode(204);
  }
  
  /**
   * As an api user
   * Given a stage exists with workitems
   * When I delete it
   * Then I receive 400
   */
  @Test
  public void testDeleteStageWithWorkItems() {
    String boardId = this.createWorkflow("Test 1").get("id");
    JsonPath stageJson = this.createStage(boardId, "Stage 1");
    String stageId = stageJson.get("id");
    this.createWorkItem(boardId, stageId, "WorkItem 1", StringUtils.EMPTY);
    
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
    .when()
      .delete("/workflows/" + boardId + "/stages/" + stageId)
    .then()
      .statusCode(400);
  }
  
  /**
   * As an api user
   * Given a board exists
   * When I create a new stage with invalid parameters
   * Then I get a 400
   * Then the stage is not created
   */
  @Test
  public void testCreateInvalidStage() {
    String boardId = this.createWorkflow("Test").get("id");
    
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(StringUtils.EMPTY)
    .when()
      .post("/workflows/" + boardId + "/stages")
    .then()
      .statusCode(400);
    
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(new StageInputModel(StringUtils.EMPTY))
    .when()
      .post("/workflows/" + boardId + "/stages")
    .then()
      .statusCode(400);
    
    String stageName = StringUtils.right("", 256);
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(new StageInputModel(stageName))
    .when()
      .post("/workflows/" + boardId + "/stages")
    .then()
      .statusCode(400);
  }
  
  /**
   * As an api user
   * Given a board with a stage exists
   * When I update this stage with invalid parameters
   * Then I receive a 400
   * Then the stage is not updated
   */
  @Test
  public void testUpdateInvalidStage() {
    String boardId = this.createWorkflow("Test 1").get("id");
    String stageId = this.createStage(boardId, "Test").get("id");
    
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(StringUtils.EMPTY)
    .when()
      .put("/workflows/" + boardId + "/stages/" + stageId)
    .then()
      .statusCode(400);
    
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(new StageInputModel(StringUtils.EMPTY))
    .when()
      .put("/workflows/" + boardId + "/stages/" + stageId)
    .then()
      .statusCode(400);
    
    String stageName = StringUtils.right("", 256);
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(new StageInputModel(stageName))
    .when()
      .put("/workflows/" + boardId + "/stages/" + stageId)
    .then()
      .statusCode(400);
  }

}
