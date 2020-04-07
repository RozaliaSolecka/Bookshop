package net.stawrul.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "orderedbooks")
@EqualsAndHashCode(of = "id")
public class OrderedBooks {

    @Id
    private UUID id = UUID.randomUUID();

    @Getter
    @Setter
    @ManyToOne
    private Book book;

    @Getter
    @Setter
    private Integer amount;
}