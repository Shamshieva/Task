package com.manas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gallery_gen")
    @SequenceGenerator(name = "gallery_gen", sequenceName = "gallery_seq", allocationSize = 1)
    private Long id;
    private byte[] imageOrVideo;
    @OneToOne
    private User user;
}
