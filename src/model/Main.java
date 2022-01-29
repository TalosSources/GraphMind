package model;

public class Main {
    public static void main(String[] args) {
        ModifiableNode Moi = new SimpleNode("Moi");
        ModifiableNode ComputerScience = new SimpleNode("CompSci");
        ModifiableNode EPFL = new SimpleNode("EPFL");
        ModifiableNode MaterialScience = new SimpleNode("MaterialScience");
        ModifiableNode BA3 = new SimpleNode("BA3");

        String epflText =
                "L'ecole polytechnique federale de Lausanne, ou je suis inscrit. Plutot pas mal dans l'ensemble.";
        String MatSciText =
                "La science qui etudie les materiaux, a priori aucun rapport avec moi.";

        EPFL.setText(epflText);
        MaterialScience.setText(MatSciText);

        ComputerScience.setName("ComputerScience");

        EPFL.setUrl("www.epfl.ch");

        connectParentChild(Moi, EPFL);
        connectParentChild(Moi, ComputerScience);
        connectParentChild(EPFL, BA3);
        connectSiblings(ComputerScience, EPFL);
        connectSiblings(MaterialScience, EPFL);

        System.out.print(Moi.textRepresentation());
        System.out.print(ComputerScience.textRepresentation());
        System.out.print(EPFL.textRepresentation());
        System.out.print(MaterialScience.textRepresentation());
        System.out.print(BA3.textRepresentation());
    }

    public static void connectParentChild(ModifiableNode parent, ModifiableNode child){
        parent.addChild(child);
        child.addParent(parent);
    }

    public static void connectSiblings(ModifiableNode s1, ModifiableNode s2) {
        s1.addSibling(s2);
        s2.addSibling(s1);
    }
}
