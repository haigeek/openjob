package io.openjob.server.admin.request.job;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author stelin swoft@qq.com
 * @since 1.0.0
 */
@Data
public class DeleteJobInstanceRequest {
    @NotEmpty
    @ApiModelProperty(value = "Delete ids", required = true)
    private List<Long> ids;
}
