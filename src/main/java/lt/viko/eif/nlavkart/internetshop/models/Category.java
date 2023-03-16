package lt.viko.eif.nlavkart.internetshop.models;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

/**
 * Category class.
 */
@XmlType(name = "categories", propOrder = {"id", "name", "description"})
@Entity
@Table(name = "category")
public class Category {
    /**
     * ID of the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    /**
     * Name of the category.
     */
    private String name;
    /**
     * Description of the category.
     */
    private String description;

    /**
     * No-Args constructor.
     */
    public Category() {
    }

    /**
     * Constructor with parameters.
     *
     * @param id          id
     * @param name        name
     * @param description description
     */
    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
     * Override toString method.
     *
     * @return output
     */
    @Override
    public String toString() {
        String output;
        output = "\t\t\tID: " + id + "\n";
        output += "\t\t\tName: " + name + "\n";
        output += "\t\t\tDescription: " + description + "\n";
        return output;
    }
}
