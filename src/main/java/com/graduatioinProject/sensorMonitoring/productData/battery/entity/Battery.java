package com.graduatioinProject.sensorMonitoring.productData.battery.entity;

import com.graduatioinProject.sensorMonitoring.productData.node.entity.Node;
import com.graduatioinProject.sensorMonitoring.productData.site.entity.Site;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * @Author : Jeeseob
 * @CreateAt : 2022/05/10
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Battery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String information;

    private String imageUrl;

    @ManyToOne(targetEntity = Site.class)
    private Site site;

    @OneToMany(targetEntity = Node.class)
    private List<Node> nodes;
}
