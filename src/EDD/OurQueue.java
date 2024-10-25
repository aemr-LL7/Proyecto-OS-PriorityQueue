package EDD;

/**
 *
 * @author B-St
 */
public class OurQueue<T> {

    private SimpleNode<T> pFirst;
    private SimpleNode<T> pLast;
    private int size;

    public OurQueue() {
        this.pFirst = null;
        this.pLast = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.pFirst == null;
    }

    public void insert(T data) {
        
        SimpleNode<T> newNode = new SimpleNode(data);

        if (this.isEmpty()) {
            this.pFirst = newNode;
            this.pLast = newNode;

        } else {
            this.pLast.setpNext(newNode);
            this.pLast = newNode;

        }

        this.size++;

    }

    public T pop() {
        SimpleNode<T> popped = this.pFirst;
        this.pFirst = this.pFirst.getpNext();
        this.size--;

        return popped.getData();
    }

    public SimpleNode<T> getpFirst() {
        return pFirst;
    }

    public void setpFirst(SimpleNode<T> pFirst) {
        this.pFirst = pFirst;
    }

    public SimpleNode<T> getpLast() {
        return pLast;
    }

    public void setpLast(SimpleNode<T> pLast) {
        this.pLast = pLast;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    

}
