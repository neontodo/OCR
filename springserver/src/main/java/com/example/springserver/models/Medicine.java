package com.example.springserver.models;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name="medicine", schema = "licensio")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "substitute0")
    private String substitute0;

    @Column(name = "substitute1")
    private String substitute1;

    @Column(name = "substitute2")
    private String substitute2;

    @Column(name = "substitute3")
    private String substitute3;

    @Column(name = "substitute4")
    private String substitute4;

    @Column(name = "sideEffect0")
    private String sideEffect0;

    @Column(name = "sideEffect1")
    private String sideEffect1;

    @Column(name = "sideEffect2")
    private String sideEffect2;

    @Column(name = "sideEffect3")
    private String sideEffect3;

    @Column(name = "sideEffect4")
    private String sideEffect4;

    @Column(name = "sideEffect5")
    private String sideEffect5;

    @Column(name = "sideEffect6")
    private String sideEffect6;

    @Column(name = "sideEffect7")
    private String sideEffect7;

    @Column(name = "sideEffect8")
    private String sideEffect8;

    @Column(name = "sideEffect9")
    private String sideEffect9;

    @Column(name = "sideEffect10")
    private String sideEffect10;

    @Column(name = "sideEffect11")
    private String sideEffect11;

    @Column(name = "sideEffect12")
    private String sideEffect12;

    @Column(name = "sideEffect13")
    private String sideEffect13;

    @Column(name = "sideEffect14")
    private String sideEffect14;

    @Column(name = "sideEffect15")
    private String sideEffect15;

    @Column(name = "sideEffect16")
    private String sideEffect16;

    @Column(name = "sideEffect17")
    private String sideEffect17;

    @Column(name = "sideEffect18")
    private String sideEffect18;

    @Column(name = "sideEffect19")
    private String sideEffect19;

    @Column(name = "sideEffect20")
    private String sideEffect20;

    @Column(name = "sideEffect21")
    private String sideEffect21;

    @Column(name = "sideEffect22")
    private String sideEffect22;

    @Column(name = "sideEffect23")
    private String sideEffect23;

    @Column(name = "sideEffect24")
    private String sideEffect24;

    @Column(name = "sideEffect25")
    private String sideEffect25;

    @Column(name = "sideEffect26")
    private String sideEffect26;

    @Column(name = "sideEffect27")
    private String sideEffect27;

    @Column(name = "sideEffect28")
    private String sideEffect28;

    @Column(name = "sideEffect29")
    private String sideEffect29;

    @Column(name = "sideEffect30")
    private String sideEffect30;

    @Column(name = "sideEffect31")
    private String sideEffect31;

    @Column(name = "sideEffect32")
    private String sideEffect32;

    @Column(name = "sideEffect33")
    private String sideEffect33;

    @Column(name = "sideEffect34")
    private String sideEffect34;

    @Column(name = "sideEffect35")
    private String sideEffect35;

    @Column(name = "sideEffect36")
    private String sideEffect36;

    @Column(name = "sideEffect37")
    private String sideEffect37;

    @Column(name = "sideEffect38")
    private String sideEffect38;

    @Column(name = "sideEffect39")
    private String sideEffect39;

    @Column(name = "sideEffect40")
    private String sideEffect40;

    @Column(name = "sideEffect41")
    private String sideEffect41;

    @Column(name = "use0")
    private String use0;

    @Column(name = "use1")
    private String use1;

    @Column(name = "use2")
    private String use2;

    @Column(name = "use3")
    private String use3;

    @Column(name = "use4")
    private String use4;

    @Column(name = "`Chemical Class`")
    private String chemical_class;

    @Column(name = "`Habit Forming`")
    private String habit_forming;

    @Column(name = "`Therapeutic Class`")
    private String therapeutic_class;

    @Column(name = "`Action Class`")
    private String action_class;
}
