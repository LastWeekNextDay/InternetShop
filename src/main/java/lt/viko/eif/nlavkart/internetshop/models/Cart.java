package lt.viko.eif.nlavkart.internetshop.models;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Cart class.
 */
@XmlType(name = "carts", propOrder = {"id", "items"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "cart")
public class Cart {
    /**
     * ID of the cart.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    /**
     * Items in the cart.
     */
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    @OneToMany(targetEntity = Item.class, cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    /**
     * No-Args constructor.
     */
    public Cart() {
    }

    /**
     * Constructor with parameters.
     *
     * @param items items
     * @param id    id
     */
    public Cart(List<Item> items, int id) {
        this.items = items;
        this.id = id;
    }

    /**
     * Get id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Set id.
     *
     * @param id id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get items.
     *
     * @return items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Set items.
     *
     * @param items items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Overridden toString method.
     *
     * @return output
     */
    @Override
    public String toString() {
        String output;
        output = "\tID: " + id + "\n";
        output += "\tItems:\n\n";
        output += "\t========ITEMS=======\n";
        for (Item item : items) {
            output += "\t\t---------item------\n";
            output += item.toString() + "\n";
            output += "\t\t---------/item-----\n\n\n";
        }
        output += "\t=======/ITEMS=======\n";
        return output;
    }
}
