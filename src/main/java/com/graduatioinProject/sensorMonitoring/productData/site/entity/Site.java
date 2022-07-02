package com.graduatioinProject.sensorMonitoring.productData.site.entity;

import com.graduatioinProject.sensorMonitoring.productData.battery.entity.Battery;
import com.graduatioinProject.sensorMonitoring.productData.site.dto.SiteResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * @Author : Jeeseob
 * @CreateAt : 2022/05/10
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String information;

    private double gpsXPos;
    private double gpsYPos;

    @OneToMany(targetEntity = Battery.class, fetch = FetchType.LAZY)
    private List<Battery> battery;

    public SiteResponse toResponse() {

        return SiteResponse
                .builder()
                .name(this.name)
                .type(this.type)
                .information(this.information)
                .gpsXPos(this.gpsXPos)
                .gpsYPos(this.gpsYPos)
                .build();
    }
}
