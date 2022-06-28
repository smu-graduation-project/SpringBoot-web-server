package com.graduatioinProject.sensorMonitoring.productData.node.controller;

import com.auth0.jwt.JWT;
import com.graduatioinProject.sensorMonitoring.baseUtil.annotation.AuthorityCheckUser;
import com.graduatioinProject.sensorMonitoring.baseUtil.config.jwt.JwtProperties;
import com.graduatioinProject.sensorMonitoring.baseUtil.config.service.JwtService;
import com.graduatioinProject.sensorMonitoring.baseUtil.dto.CommonResult;
import com.graduatioinProject.sensorMonitoring.baseUtil.dto.SingleResult;
import com.graduatioinProject.sensorMonitoring.baseUtil.exception.BussinessException;
import com.graduatioinProject.sensorMonitoring.baseUtil.exception.ExMessage;
import com.graduatioinProject.sensorMonitoring.baseUtil.service.ResponseService;
import com.graduatioinProject.sensorMonitoring.productData.battery.service.BatteryService;
import com.graduatioinProject.sensorMonitoring.productData.node.dto.NodeResponse;
import com.graduatioinProject.sensorMonitoring.productData.node.entity.Node;
import com.graduatioinProject.sensorMonitoring.productData.node.service.NodeService;
import com.graduatioinProject.sensorMonitoring.productData.node.dto.NodeUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "06. 노드")
@RequestMapping("api/product/node")
@RequiredArgsConstructor
@RestController
public class NodeController {

    private final NodeService nodeService;
    private final BatteryService batteryService;
    private final ResponseService responseService;
    private final JwtService jwtService;

    @ApiOperation(value = "노드 상세정보", notes = "노드 id를 받아 해당하는 노드의 상세정보를 반환")
    @GetMapping("/detail/{id}")
    public SingleResult<NodeResponse> getNodeDetail(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        
//        String accessToken = httpServletRequest.getHeader("Authorization");
//        Long memberId = JWT.decode(accessToken).getClaim(JwtProperties.ID).asLong();
//
        NodeResponse response = nodeService.findByIdResponse(id);
        try {
            return responseService.singleResult(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BussinessException(e.getMessage());
        }

    }

    @ApiOperation(value = "노드 추가", notes = "노드 관련 정보를 받아 노드를 추가(프론트에서 처리 X)")
    @PostMapping("/add/{port}")
    public SingleResult<String> setNodeDetail(@PathVariable Long port){
        /**
         * 노드 정보는 자동으로 추가되도록 하고,
         * 이를 수정하는 것을 직접 하도록 설정하는 것은 어떤지??
         * 그렇게 하려면, 특정 IP만 가능하도록 설정하거나, 따로 암호화된 키를 가져야 할 것 같음.
         */
        try {
            Long id = nodeService.setNode(Node.builder().port(port).build());
            return responseService.singleResult(String.valueOf(id));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BussinessException(e.getMessage());
        }
    }

    @ApiOperation(value = "노드 수정", notes = "노드 관련 정보를 받아 노드정보를 수정합니다.")
    @PutMapping("/update/{id}")
    public CommonResult setNodeDetail( HttpServletRequest httpServletRequest,
                                       @RequestBody NodeUpdateRequest request,
                                       @PathVariable Long id) {

        if (!nodeService.checkNode(id)) {
            return responseService.failResult(ExMessage.NODE_ERROR_NOT_FOUND.getMessage());
        }

        try {
            batteryService.getBattery(request.getBatteryId()); // 해당하는 배터리가 없다면 service단에서 에러메세지
            nodeService.setNode(request.toEntity());
            return responseService.successResult();

        } catch (Exception e) {
            e.printStackTrace();
            throw new BussinessException(e.getMessage());
        }
    }
}
