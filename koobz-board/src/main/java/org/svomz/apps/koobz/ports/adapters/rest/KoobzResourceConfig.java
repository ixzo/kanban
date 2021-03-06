package org.svomz.apps.koobz.ports.adapters.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
import org.svomz.apps.koobz.ports.adapters.rest.exceptions.WorkflowNotFoundMapper;
import org.svomz.apps.koobz.ports.adapters.rest.exceptions.EntityNotFoundMapper;
import org.svomz.apps.koobz.ports.adapters.rest.exceptions.IllegalArgumentMapper;
import org.svomz.apps.koobz.ports.adapters.rest.exceptions.StageNotEmptyMapper;
import org.svomz.apps.koobz.ports.adapters.rest.exceptions.StageNotInProcessMapper;
import org.svomz.apps.koobz.ports.adapters.rest.exceptions.WorkItemNotArchivedMapper;
import org.svomz.apps.koobz.ports.adapters.rest.exceptions.WorkItemNotInProcessMapper;
import org.svomz.apps.koobz.ports.adapters.rest.resources.WorkflowResource;
import org.svomz.apps.koobz.ports.adapters.rest.resources.StageResource;
import org.svomz.apps.koobz.ports.adapters.rest.resources.WorkItemResource;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/")
public class KoobzResourceConfig extends ResourceConfig {

  public KoobzResourceConfig() {
    this.register(WorkflowResource.class);
    this.register(StageResource.class);
    this.register(WorkItemResource.class);
    this.register(JacksonFeature.class);
    this.register(EntityNotFoundMapper.class);
    this.register(IllegalArgumentMapper.class);
    this.register(StageNotEmptyMapper.class);
    this.register(StageNotInProcessMapper.class);
    this.register(WorkItemNotInProcessMapper.class);
    this.register(WorkflowNotFoundMapper.class);
    this.register(WorkItemNotArchivedMapper.class);
  }

}
