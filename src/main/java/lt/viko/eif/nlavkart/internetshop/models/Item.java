package lt.viko.eif.nlavkart.internetshop.models;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

/**
 * Item class.
 */
@XmlType(name = "items", propOrder = {"id", "name", "description", "category", "price", "quantity"})
@Entity
@Table(name = "item")
public class Item {
    /**
     * ID of the item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    /**
     * Name of the item.
     */
    private String name;
    /**
     * Description of the item.
     */
    private String description;
    /**
     * Category of the item.
     */
    @OneToOne(targetEntity = Category.class, cascade = CascadeType.ALL)
    private Category category;
    /**
     * Price of the item.
     */
    private double price;
    /**
     * Quantity of the item.
     */
    private int quantity;

    /**
     * No-Args constructor.
     */
    public Item() {
    }

    /**
     * Constructor with parameters.
     *
     * @param id          id
     * @param name        name
     * @param description description
     * @param category    category
     * @param price       price
     * @param quantity    quantity
     */
    public Item(int id, String name, String description, Category category, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
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
     * Get name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set name.
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description.
     *
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get category.
     *
     * @return category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Set category.
     *
     * @param category category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Get price.
     *
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Set price.
     *
     * @param price price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Get quantity.
     *
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Set quantity.
     *
     * @param quantity quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Overriden toString method.
     *
     * @return output
     */
    @Override
    public String toString() {
        String output;
        output = "\t\tID: " + id + "\n";
        output += "\t\tName: " + name + "\n";
        output += "\t\tDescription: " + description + "\n";
        output += "\t\tCategory:\n\n" + category + "\n";
        output += "\t\tPrice: " + price + "\n";
        output += "\t\tQuantity: " + quantity + "\n";
        return output;
    }
}
