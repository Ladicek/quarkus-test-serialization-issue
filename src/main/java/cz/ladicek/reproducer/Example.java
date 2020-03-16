package cz.ladicek.reproducer;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Example extends PanacheEntity {
    public String text;
}
