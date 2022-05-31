package com.graduatioinProject.sensorMonitoring.productData.node.controller;

import com.graduatioinProject.sensorMonitoring.baseUtil.annotation.LoginCheck;
import com.graduatioinProject.sensorMonitoring.baseUtil.dto.CommonResult;
import com.graduatioinProject.sensorMonitoring.baseUtil.dto.SingleResult;
import com.graduatioinProject.sensorMonitoring.baseUtil.exception.BussinessException;
import com.graduatioinProject.sensorMonitoring.baseUtil.exception.ExMessage;
import com.graduatioinProject.sensorMonitoring.baseUtil.service.ResponseService;
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
    private final ResponseService responseService;

    @LoginCheck
    @ApiOperation(value = "노드 상세정보", notes = "노드 id를 받아 해당하는 노드의 상세정보를 반환")
    @GetMapping("/detail/{id}")
    public SingleResult<NodeResponse> getNodeDetail(HttpServletRequest httpServletRequest,
                                                    @PathVariable Long id) {

        NodeResponse response = nodeService.getNodeResponse(id);
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

    @LoginCheck
    @ApiOperation(value = "노드 수정", notes = "노드 관련 정보를 받아 노드정보를 수정합니다.")
    @PutMapping("/update/{id}")
    public CommonResult setNodeDetail( HttpServletRequest httpServletRequest,
                                       @RequestBody NodeUpdateRequest request,
                                       @PathVariable Long id) {

        /**
         * 권헌 확인
         * + 해당하는 id의 배터리가 있는지
         */

        if(!nodeService.checkNode(id)) {
            return responseService.failResult(ExMessage.NODE_ERROR_NOT_FOUND.getMessage());
        }

        try {
            nodeService.setNode(request.toEntity());
            return  responseService.successResult();

        } catch (Exception e) {
            e.printStackTrace();
            throw new BussinessException(e.getMessage());
        }
    }
}
