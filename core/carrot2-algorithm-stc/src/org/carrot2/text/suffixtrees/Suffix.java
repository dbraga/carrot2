package org.carrot2.text.suffixtrees;

/**
 * A suffix object used internally in class {@link SuffixTree}.
 */
final class Suffix
{
    /** The owner of this suffix. */
    protected final SuffixTree container;

    /** first collection element where this suffix starts */
    protected int firstElementIndex;

    /** the collection element where this suffix ends (inclusive) */
    protected int lastElementIndex;

    /** origin node needed for canonisation operation */
    protected Node originNode;

    /**
     * 
     */
    public Suffix(SuffixTree container, Node originNode, int firstElementIndex,
        int lastElementIndex)
    {
        this.container = container;
        this.originNode = originNode;
        this.firstElementIndex = firstElementIndex;
        this.lastElementIndex = lastElementIndex;
    }

    /**
     * Returns <code>true</code> when this suffix ends at an explicit node.
     */
    public boolean isExplicit()
    {
        return (firstElementIndex > lastElementIndex);
    }

    /**
     * Returns <code>true</code> when this suffix ends in an implicit node (node not
     * ending on the branch of the tree).
     */
    public boolean isImplicit()
    {
        return (firstElementIndex <= lastElementIndex);
    }

    /**
     * Performs canonization of this suffix. We slide down the tree until we reach the
     * closest node to the end of the collection.
     */
    public void canonize()
    {
        if (!isExplicit())
        {
            Edge edge = originNode.findEdgeMatchingFirstElement(container
                .getCurrentElement().get(firstElementIndex));
            int edgeSpan = edge.lastElementIndex - edge.firstElementIndex;

            while (edgeSpan <= (lastElementIndex - firstElementIndex))
            {
                firstElementIndex = firstElementIndex + edgeSpan + 1;
                originNode = edge.endNode;

                if (firstElementIndex <= lastElementIndex)
                {
                    edge = originNode.findEdgeMatchingFirstElement(container
                        .getCurrentElement().get(firstElementIndex));

                    if (edge == null)
                    {
                        throw new RuntimeException();
                    }

                    edgeSpan = edge.lastElementIndex - edge.firstElementIndex;
                }
            }
        }
    }
}
