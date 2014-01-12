package org.kframework.kil;

import org.kframework.kil.loader.Context;
import org.kframework.kil.matchers.Matcher;
import org.kframework.kil.visitors.Transformer;
import org.kframework.kil.visitors.Visitor;
import org.kframework.kil.visitors.exceptions.TransformerException;

import java.util.Collection;
import java.util.Collections;


/**
 * Builtin List update operation.
 *
 * @author TraianSF (refactoring from {@link SetUpdate})
 */
public class ListUpdate extends Term {

    /** {@link Variable} name of the set */
    private final Variable base;

    /** {@code List} of entries to be removed from the list */
    private final Collection<Term> removeLeft;
    private final Collection<Term> removeRight;

    public ListUpdate(Variable base, Collection<Term> removeLeft, Collection<Term> removeRight) {
        super(base.getSort());
        this.base = base;
        this.removeLeft = removeLeft;
        this.removeRight = removeRight;
    }

    public Variable base() {
        return base;
    }

    public Collection<Term> removeLeft() {
        return Collections.unmodifiableCollection(removeLeft);
    }

    public Collection<Term> removeRight() {
        return Collections.unmodifiableCollection(removeRight);
    }

    @Override
    public Term shallowCopy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * Context.HASH_PRIME + base.hashCode();
        hash = hash * Context.HASH_PRIME + removeLeft.hashCode();
        hash = hash * Context.HASH_PRIME + removeRight.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ListUpdate)) {
            return false;
        }

        ListUpdate listUpdate = (ListUpdate) object;
        return base.equals(listUpdate.base)
                && removeLeft == listUpdate.removeLeft && removeRight == listUpdate.removeRight;
    }

    @Override
    public void accept(Matcher matcher, Term toMatch) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ASTNode accept(Transformer transformer) throws TransformerException {
        return transformer.transform(this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

	@Override
	public Term kilToKore() {
		
		KLabel tempLabel = new KLabelConstant("_[_/_]");
		
		java.util.List<Term> leftList = new java.util.ArrayList<Term>(this.removeLeft);
		java.util.List<Term> rightList = new java.util.ArrayList<Term>(this.removeRight);
		
		for(int i=0;i<leftList.size();++i){
			
			leftList.set(i, KSequence.adjust(leftList.get(i).kilToKore()));
		}
		
		for(int i=0;i<rightList.size();++i){
			
			rightList.set(i, KSequence.adjust(rightList.get(i).kilToKore()));
		}
		
		KApp leftItem = new KApp(new KLabelConstant("List"),new KList(leftList));
		KApp rightItem = new KApp(new KLabelConstant("List"),new KList(rightList));
		
		java.util.List<Term> tempList = new java.util.ArrayList<Term>();
		tempList.add(KList.adjust(this.base.kilToKore()));
		tempList.add(leftItem);
		tempList.add(rightItem);
		
		return new KApp(tempLabel,new KList(tempList));
	}

}
