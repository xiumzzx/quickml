package quickml.supervised.classifier.tree.decisionTree.tree;

import com.google.common.collect.Maps;
import org.javatuples.Pair;
import quickml.collections.ValueSummingMap;
import quickml.data.InstanceWithAttributesMap;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;


public class ClassificationCounter extends TermStatistics implements Serializable {
    private static final long serialVersionUID = -6821237234748044623L;
    private final ValueSummingMap<Object> counts = new ValueSummingMap<Object>();

    public ClassificationCounter() {
    }

    public ClassificationCounter(ClassificationCounter classificationCounter) {
        this.counts.putAll(classificationCounter.counts);
    }
    public ClassificationCounter(HashMap<Object, ? extends Number> mapOfCounts) {
        for (Object classification: mapOfCounts.keySet()) {
            counts.addToValue(classification, mapOfCounts.get(classification).doubleValue());
        }
    }

    public static ClassificationCounter merge(ClassificationCounter a, ClassificationCounter b) {
        ClassificationCounter newCC = new ClassificationCounter();
        newCC.counts.putAll(a.counts);
        for (Entry<Object, Number> e : b.counts.entrySet()) {
            newCC.counts.addToValue(e.getKey(), e.getValue().doubleValue());
        }
        return newCC;
    }

    //should be abstracted.  Data should be in an inner class


    public Map<Object, Double> getCounts() {
        Map<Object, Double> ret = Maps.newHashMap();
        for (Entry<Object, Number> serializableNumberEntry : counts.entrySet()) {
            ret.put(serializableNumberEntry.getKey(), serializableNumberEntry.getValue().doubleValue());
        }
        return ret;
    }


    public static ClassificationCounter countAll(final Iterable<? extends InstanceWithAttributesMap> instances) {
        final ClassificationCounter result = new ClassificationCounter();
        for (InstanceWithAttributesMap instance : instances) {
            result.addClassification(instance.getLabel(), instance.getWeight());
        }
        return result;
    }

    public void addClassification(final Object classification, double weight) {
        counts.addToValue(classification, weight);
    }

    public double getCount(final Object classification) {
        Number count = counts.get(classification);
        if (count == null) {
            return 0;
        } else {
            return count.doubleValue();
        }
    }

    public Set<Object> allClassifications() {
        return counts.keySet();
    }

    public ClassificationCounter add(final ClassificationCounter other) {
        final ClassificationCounter result = new ClassificationCounter();
        result.counts.putAll(counts);
        for (final Entry<Object, Number> e : other.counts.entrySet()) {
            result.counts.addToValue(e.getKey(), e.getValue().doubleValue());
        }
        return result;
    }

    public ClassificationCounter subtract(final ClassificationCounter other) {
        final ClassificationCounter result = new ClassificationCounter();
        result.counts.putAll(counts);
        for (final Entry<Object, Number> e : other.counts.entrySet()) {
            result.counts.addToValue(e.getKey(), -other.getCount(e.getKey()));
        }
        return result;
    }

    public double getTotal() {
        return counts.getSumOfValues();
    }

    public Pair<Object, Double> mostPopular() {
        Entry<Object, Number> best = null;
        for (final Entry<Object, Number> e : counts.entrySet()) {
            if (best == null || e.getValue().doubleValue() > best.getValue().doubleValue()) {
                best = e;
            }
        }
        return Pair.with(best.getKey(), best.getValue().doubleValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassificationCounter that = (ClassificationCounter) o;

        if (!counts.equals(that.counts)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return counts.hashCode();
    }
}
