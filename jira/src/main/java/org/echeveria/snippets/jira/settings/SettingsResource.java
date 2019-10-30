package org.echeveria.snippets.jira.settings;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;

/**
 * A resource of message.
 */
@Path("/message")
public class SettingsResource {

  @GET
  @AnonymousAllowed
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  public Response getMessage() {
    return Response.ok(new SettingsResourceModel("Hello World")).build();
  }

}
