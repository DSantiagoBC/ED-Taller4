
import java.util.*;

class Ejecucion {

    public Ejecucion() {

    }

    public static void main(String[] args) {
        Ejecucion empezar = new Ejecucion();
    }

}

class AVLtree<T extends Comparable<? super T>> {

    private AVLnode<T> root;

    public AVLtree() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void insert(T x) {
        root = insert(root, x);
    }

    private AVLnode<T> insert(AVLnode<T> nodo, T x) {
        if (nodo == null) {
            return new AVLnode<T>(x);
        }
        int comparation = x.compareTo(nodo.getElement());
        if (comparation < 0) {
            nodo.setLeftChild(insert(nodo.getLeftChild(), x));
        } else if (comparation > 0) {
            nodo.setRightChild(insert(nodo.getRightChild(), x));
        }
        return balance(nodo);
    }

    public void remove(T x) {
        root = remove(root, x);
    }

    private AVLnode<T> remove(AVLnode<T> nodo, T x) {
        if (nodo == null) {
            return null;
        }
        int comparation = x.compareTo(nodo.getElement());
        if (comparation < 0) {
            nodo.setLeftChild(remove(nodo.getLeftChild(), x));
        } else if (comparation > 0) {
            nodo.setRightChild(remove(nodo.getRightChild(), x));
        } else {
            if (nodo.getLeftChild() != null && nodo.getRightChild() != null) {
                nodo.setElement(findMin(nodo.getRightChild()));
                nodo.setRightChild(remove(nodo.getRightChild(), nodo.getElement()));
            } else {
                nodo = (nodo.getLeftChild() != null) ? nodo.getLeftChild() : nodo.getRightChild();
            }
        }
        return balance(nodo);
    }

    public T findMin() {
        return findMin(root);
    }

    public T findMin(AVLnode<T> nodoInicial) {
        AVLnode<T> nodo = nodoInicial;
        T elemento = null;
        while (nodo != null) {
            elemento = nodo.getElement();
            nodo = nodo.getLeftChild();
        }
        return elemento;
    }

    public AVLnode<T> search(T x) {
        AVLnode<T> nodo = root;
        while (nodo != null) {
            int comparation = x.compareTo(nodo.getElement());
            if (comparation == 0) {
                return nodo;
            }
            if (comparation < 0) {
                nodo = nodo.getLeftChild();
            } else {
                nodo = nodo.getRightChild();
            }
        }
        return nodo;
    }

    public boolean contains(T x) {
        return !(search(x) == null);
    }

    private int height() {
        return height(root);
    }

    private int height(AVLnode<T> nodo) {
        return nodo == null ? -1 : nodo.getHeight();
    }

    public void printTree() {
        if (isEmpty()) {
            System.out.println("Empty tree");
        } else {
            printTree(root);
        }
    }

    private void printTree(AVLnode<T> nodo) {
        if (nodo != null) {
            printTree(nodo.getLeftChild());
            System.out.println(nodo);
            printTree(nodo.getRightChild());
        }
    }

    private AVLnode<T> rotateWithLeftChild(AVLnode<T> k2) {
        AVLnode<T> k1 = k2.getLeftChild();
        k2.setLeftChild(k1.getRightChild());
        k1.setRightChild(k2);
        k2.setHeight(Math.max(height(k2.getLeftChild()), height(k2.getRightChild())) + 1);
        k1.setHeight(Math.max(height(k1.getLeftChild()), k2.getHeight()) + 1);
        return k1;
    }

    private AVLnode<T> rotateWithRightChild(AVLnode<T> k1) {
        AVLnode<T> k2 = k1.getRightChild();
        k1.setRightChild(k2.getLeftChild());
        k2.setLeftChild(k1);
        k1.setHeight(Math.max(height(k1.getLeftChild()), height(k1.getRightChild())) + 1);
        k2.setHeight(Math.max(height(k2.getRightChild()), k1.getHeight()) + 1);
        return k2;
    }

    private AVLnode<T> doubleWithLeftChild(AVLnode<T> k3) {
        k3.setLeftChild(rotateWithRightChild(k3.getLeftChild()));
        return rotateWithLeftChild(k3);
    }

    private AVLnode<T> doubleWithRightChild(AVLnode<T> k1) {
        k1.setRightChild(rotateWithLeftChild(k1.getRightChild()));
        return rotateWithRightChild(k1);
    }

    private static final int ALLOWED_IMBALANCE = 1;

    private AVLnode<T> balance(AVLnode<T> nodo) {
        if (nodo == null) {
            return nodo;
        }

        if (height(nodo.getLeftChild()) - height(nodo.getRightChild()) > ALLOWED_IMBALANCE) {
            if (height(nodo.getLeftChild().getLeftChild()) >= height(nodo.getLeftChild().getRightChild())) {
                nodo = rotateWithLeftChild(nodo);
            } else {
                nodo = doubleWithLeftChild(nodo);
            }
        } else if (height(nodo.getRightChild()) - height(nodo.getLeftChild()) > ALLOWED_IMBALANCE) {
            if (height(nodo.getRightChild().getRightChild()) >= height(nodo.getRightChild().getLeftChild())) {
                nodo = rotateWithRightChild(nodo);
            } else {
                nodo = doubleWithRightChild(nodo);
            }
        }

        nodo.setHeight(Math.max(height(nodo.getLeftChild()), height(nodo.getRightChild())) + 1);
        return nodo;
    }

    public void checkBalance() {
        checkBalance(root);
    }

    private int checkBalance(AVLnode<T> nodo) {
        if (nodo == null) {
            return -1;
        }

        int hl = checkBalance(nodo.getLeftChild());
        int hr = checkBalance(nodo.getRightChild());
        if (Math.abs(height(nodo.getLeftChild()) - height(nodo.getRightChild())) > 1
                || height(nodo.getLeftChild()) != hl || height(nodo.getRightChild()) != hr) {
            System.out.println("Arbol no balanceado :(");
        }

        return height(nodo);
    }
}

class AVLnode<T> {

    T element;
    AVLnode<T> leftChild;
    AVLnode<T> rightChild;
    int height;

    public AVLnode() {
        this(null, null, null);
    }

    public AVLnode(T theElement) {
        this(theElement, null, null);
    }

    public AVLnode(T theElement, AVLnode<T> theLeftChild, AVLnode<T> theRightChild) {
        element = theElement;
        leftChild = theLeftChild;
        rightChild = theRightChild;
        height = 0; //no ha sido definido
    }

    public AVLnode<T> getLeftChild() {
        return leftChild;
    }

    public AVLnode<T> getRightChild() {
        return rightChild;
    }

    public T getElement() {
        return element;
    }

    public int getHeight() {
        return height;
    }

    public void setLeftChild(AVLnode<T> theLeftChild) {
        leftChild = theLeftChild;
    }

    public void setRightChild(AVLnode<T> theRightChild) {
        rightChild = theRightChild;
    }

    public void setElement(T theElement) {
        element = theElement;
    }

    public void setHeight(int theHeight) {
        height = theHeight;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}

class Estudiante {

    private String documento;
    private String materia;
    private String motivo;
    private int creditosAnteriores;
    private double PAPPI;

    public Estudiante(String documento, String materia, String motivo, int creditosAnteriores, double PAPPI) {
        this.documento = documento;
        this.materia = materia;
        this.motivo = motivo;
        this.creditosAnteriores = creditosAnteriores;
        this.PAPPI = PAPPI;
    }

    public String getDocumento() {
        return documento;
    }

    public String getMateria() {
        return materia;
    }

    public String getMotivo() {
        return motivo;
    }

    public int getCreditosAnteriores() {
        return creditosAnteriores;
    }

    public double getPAPPI() {
        return PAPPI;
    }
}
