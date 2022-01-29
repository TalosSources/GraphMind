package model;
import java.util.Set;
import java.util.TreeSet;

/**
 * Simple node implementation using TreeSets.
 */
class SimpleNode implements ModifiableNode {
    final private Set<Node> siblings = new TreeSet<>();
    final private Set<Node> parents = new TreeSet<>();
    final private Set<Node> children = new TreeSet<>();

    private String name;
    private String text;
    private String url;

    public SimpleNode(String name){
        this.name = name;
    }


    @Override
    public Set<Node> siblings() {
        return Set.copyOf(siblings);
    }

    @Override
    public Set<Node> parents() {
        return Set.copyOf(parents);
    }

    @Override
    public Set<Node> children() {
        return Set.copyOf(children);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void addSibling(Node sibling) {
        siblings.add(sibling);
    }

    @Override
    public void addParent(Node parent) {
        parents.add(parent);
    }

    @Override
    public void addChild(Node child) {
        children.add(child);
    }

    @Override
    public boolean removeSibling(Node sibling) {
        return siblings.remove(sibling);
    }

    @Override
    public boolean removeParent(Node parent) {
        return parents.remove(parent);
    }

    @Override
    public boolean removeChild(Node child) {
        return children.remove(child);
    }

    @Override
    public int compareTo(Node o) {
        return name().compareTo(o.name());
    }

    @Override
    public String toString() {
        return name();
    }

    public String print() {
        return name + " :\n  Siblings: " + siblings + "\n  Parents: " + parents +
                "\n  Children: " + children + "\n  Text: " + text + "\n  Url: " + url + "\n\n";
    }
}
