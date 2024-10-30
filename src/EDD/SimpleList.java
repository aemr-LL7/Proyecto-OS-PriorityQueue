/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

/**
 *
 * @author andre
 * @param <T>
 */
public class SimpleList<T> {

    private SimpleNode<T> pFirst;
    private SimpleNode<T> pLast;
    private int size;

    public SimpleList() {
        this.pFirst = null;
        this.pLast = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.getpFirst() == null;
    }

    //Anadir al inicio
    public void addStart(T data) {

        SimpleNode<T> node = new SimpleNode<>(data);

        if (this.isEmpty()) {
            this.setpFirst(node);
            this.setpLast(node);
        } else {
            node.setpNext(this.getpFirst());
            this.setpFirst(node);
        }

        this.setSize(this.getSize() + 1);

    }

    //Anadir al final
    public void addAtTheEnd(T data) {

        SimpleNode<T> node = new SimpleNode<>(data);
        if (this.isEmpty()) {
            this.setpFirst(node);
            this.setpLast(node);
        } else {
            this.getpLast().setpNext(node);
            this.setpLast(node);
        }
        this.setSize(this.getSize() + 1);
    }

    //recorrer hasta nulo
    public void printList() {
        if (this.isEmpty()) {
            System.out.println("La lista esta vacia");
        } else {
            try {
                SimpleNode<T> pAux = this.getpFirst();
                System.out.println("Lista==============================");
                while (pAux != null) {
                    System.out.println(pAux.getData());
                    pAux = pAux.getpNext();
                }
                System.out.println("===================================");
            } catch (Exception e) {
                System.out.println("error al printear.");
            }
        }
    }

    // devuelve una string que representa la lista
    public String printToString() {
        if (this.isEmpty()) {
            return "//";
        }
        SimpleNode<T> pAux = this.getpFirst();
        //int contador = 0;
        String chain = "";
        while (pAux != null) {
            //System.out.print(contador + ".[ " + pAux.getData() + " ]" + " ->  ");
            chain += pAux.getData() + "->";
            pAux = pAux.getpNext();
            //contador++;
        }
        return chain + "//";
    }

    public T getValueByIndex(int index) {
        SimpleNode<T> pAux = this.getpFirst();
        int count = 0;

        while (pAux != null && count != index) {
            pAux = pAux.getpNext();
            count++;
        }

        if (pAux != null) {
            return pAux.getData();
        } else {
            return null;
        }

    }

    public boolean contains(String value) {
        SimpleNode<T> pAux = this.getpFirst();

        while (pAux != null) {
            if (pAux.getData().equals(value)) {
                return true;
            }
            pAux = pAux.getpNext();
        }

        return false;
    }

    public boolean contains(int value) {
        SimpleNode<T> pAux = this.getpFirst();

        while (pAux != null) {
            if (pAux.getData().equals(value)) {
                return true;
            }
            pAux = pAux.getpNext();
        }

        return false;
    }

    public SimpleNode<T> searchByValue(T value) {
        SimpleNode<T> pAux = this.getpFirst();

        while (pAux != null && pAux.getData() != value) {
            pAux = pAux.getpNext();
        }

        if (pAux != null) {
            return pAux;
        } else {
            return null;
        }

    }

    /*
        Metodo para retornar la posicion de un elemento en la lista
     */
    public int indexOf(T valorBuscado) {
        SimpleNode<T> actual = this.getpFirst();
        int index = 0;

        while (actual != null) {
            if (actual.getData().equals(valorBuscado)) {
                return index;
            }

            actual = actual.getpNext();
            index++;
        }

        return -1; // Si no se encuentra el valor, devolvemos -1
    }

    public void deleteFirst() {
        if (this.isEmpty()) {
            System.out.println("Lista vacia.");
        } else {
            setpFirst((SimpleNode<T>) getpFirst().getpNext());
        }
    }

    public void deleteLast() {

        if (this.getpFirst() == this.getpLast()) {
            this.setpFirst(null);
            this.setpLast(null);
            this.setSize(0);

        } else {

            SimpleNode<T> pAux = this.getpFirst();

            while (pAux.getpNext().getpNext() != null) {
                pAux = pAux.getpNext();
            }

            pAux.setpNext(null);
            this.setpLast(pAux);
        }
    }

    public boolean isValidIndex(int index) {
        return this.getValueByIndex(index) != null;
    }

    public void insertAtIndex(int index, T data) {

        if (!isValidIndex(index)) {
            System.out.println("No existe el indice");
        } else {

            SimpleNode<T> pAux = this.getpFirst();
            int counter = 0;

            while (counter != index) {
                pAux = pAux.getpNext();
                counter++;
            }

            SimpleNode<T> node = new SimpleNode<>(data);
            node.setpNext(pAux.getpNext());

            pAux.setpNext(node);
        }
    }

    // elimina un elemento segun su indice en la lista (posicion)
    public void deleteByIndex(int index) {
        if (!this.isEmpty()) {
            if (index == 0) {
                SimpleNode<T> head = this.getpFirst();
                this.setpFirst((SimpleNode<T>) this.getpFirst().getpNext());
                head.setpNext(null);
                this.setSize(this.getSize() - 1);
            } else if (index < this.getSize()) {
                SimpleNode<T> pAux = this.getpFirst();
                int count = 0;
                while (count < (index - 1)) {
                    pAux = pAux.getpNext();
                    count++;
                }
                SimpleNode<T> temporal = pAux.getpNext();
                pAux.setpNext(temporal.getpNext());
                temporal.setpNext(null);
                this.setSize(this.getSize() - 1);
            }
        }
    }

    // ordena la lista 
    public void sort() {
        SimpleNode<Integer> pAux = (SimpleNode<Integer>) this.getpFirst();

        if (pAux == null || pAux.getpNext() == null) {
            return;
        }

        SimpleNode pPrev = null;

        while (pAux != null) {
            SimpleNode<Integer> next = pAux.getpNext();
            if (pAux.getData() <= next.getData()) {
                ;
            } else {
                swapIntegers(pAux, next);
            }
            pPrev = pAux;
            pAux = next;
        }

        sort();
    }

    private void swapIntegers(SimpleNode<Integer> current, SimpleNode<Integer> next) {
        int aux = current.getData();
        current.setData((Integer) current.getpNext().getData());
        next.setData(aux);

    }

    public void wipeList() {
        this.setpFirst(null);
        this.setSize(0);
    }

    public Object[] toArray() {
        Object[] array = new Object[this.getSize()];
        SimpleNode<T> pAux = this.getpFirst();

        for (int i = 0; i < this.getSize(); i++) {
            array[i] = pAux.getData();
            pAux = pAux.getpNext();
        }

        return array;
    }

    public void delete(T data) {

        SimpleNode<T> currentNode = this.getpFirst();
        SimpleNode<T> previousNode = null;

        while (currentNode != null && !currentNode.getData().equals(data)) {
            previousNode = currentNode;
            currentNode = currentNode.getpNext();
        }

        if (currentNode != null) {
            if (previousNode == null) {
                this.setpFirst(this.pFirst.getpNext());
            } else {
                previousNode.setpNext(currentNode.getpNext());
            }
            this.setSize(this.getSize() - 1);
        }
    }

    /**
     * @return the pFirst
     */
    public SimpleNode<T> getpFirst() {
        return pFirst;
    }

    /**
     * @param pFirst the pFirst to set
     */
    public void setpFirst(SimpleNode<T> pFirst) {
        this.pFirst = pFirst;
    }

    /**
     * @return the pLast
     */
    public SimpleNode<T> getpLast() {
        return pLast;
    }

    /**
     * @param pLast the pLast to set
     */
    public void setpLast(SimpleNode<T> pLast) {
        this.pLast = pLast;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    

}
