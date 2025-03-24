package com.example.springserver.models;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name="usermedicinehistory", schema = "licensio")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="medicine_id")
    private Long medicineId;
}
