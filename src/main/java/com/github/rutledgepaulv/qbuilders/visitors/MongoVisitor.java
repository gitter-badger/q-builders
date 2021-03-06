package com.github.rutledgepaulv.qbuilders.visitors;

import com.github.rutledgepaulv.qbuilders.nodes.AndNode;
import com.github.rutledgepaulv.qbuilders.nodes.ComparisonNode;
import com.github.rutledgepaulv.qbuilders.nodes.OrNode;
import com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator;
import org.springframework.data.mongodb.core.query.Criteria;

import java.sql.Date;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@SuppressWarnings("WeakerAccess")
public class MongoVisitor extends NodeVisitor<Criteria> {

    protected final Function<Object, Object> normalizer;

    public MongoVisitor() {
        this(DefaultNormalizer.INSTANCE);
    }

    public MongoVisitor(Function<Object, Object> normalizer) {
        this.normalizer = normalizer;
    }

    @Override
    protected Criteria visit(AndNode node) {
        Criteria criteria = new Criteria();
        List<Criteria> children = node.getChildren().stream()
                .map(this::visitAny).collect(Collectors.toList());
        return criteria.andOperator(children.toArray(new Criteria[children.size()]));
    }

    @Override
    protected Criteria visit(OrNode node) {
        Criteria criteria = new Criteria();
        List<Criteria> children = node.getChildren().stream()
                .map(this::visitAny).collect(Collectors.toList());
        return criteria.orOperator(children.toArray(new Criteria[children.size()]));
    }

    @Override
    protected Criteria visit(ComparisonNode node) {

        ComparisonOperator operator = node.getOperator();

        Collection<?> values = node.getValues().stream().map(normalizer).collect(Collectors.toList());

        if(ComparisonOperator.EQ.equals(operator)) {
            return where(node.getField()).is(single(values));
        } else if(ComparisonOperator.NE.equals(operator)) {
            return where(node.getField()).ne(single(values));
        } else if (ComparisonOperator.EX.equals(operator)) {
            return where(node.getField()).exists((Boolean)single(values));
        } else if (ComparisonOperator.GT.equals(operator)) {
            return where(node.getField()).gt(single(values));
        } else if (ComparisonOperator.LT.equals(operator)) {
            return where(node.getField()).lt(single(values));
        } else if (ComparisonOperator.GTE.equals(operator)) {
            return where(node.getField()).gte(single(values));
        } else if (ComparisonOperator.LTE.equals(operator)) {
            return where(node.getField()).lte(single(values));
        } else if (ComparisonOperator.IN.equals(operator)) {
            return where(node.getField()).in(values);
        } else if (ComparisonOperator.NIN.equals(operator)) {
            return where(node.getField()).nin(values);
        } else if (ComparisonOperator.SUB_CONDITION_ANY.equals(operator)) {
            return where(node.getField()).elemMatch(condition(node));
        }

        throw new UnsupportedOperationException("This visitor does not support the operator " + operator + ".");
    }


    protected Object single(Collection<?> values) {
        if(!values.isEmpty()) {
            return values.iterator().next();
        } else {
            throw new IllegalArgumentException("You must provide a non-null query value for the condition.");
        }
    }



    protected static class DefaultNormalizer implements Function<Object, Object> {

        protected static DefaultNormalizer INSTANCE = new DefaultNormalizer();

        @Override
        public Object apply(Object o) {
            if(o instanceof Instant) {
                return Date.from((Instant) o);
            }
            return o;
        }

    }


}
