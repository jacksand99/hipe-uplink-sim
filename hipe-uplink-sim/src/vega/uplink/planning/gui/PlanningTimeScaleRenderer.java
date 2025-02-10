package vega.uplink.planning.gui;

import java.util.ArrayList;

import de.jaret.util.date.iterator.DateIterator;
import de.jaret.util.date.iterator.*;
import de.jaret.util.ui.timebars.swing.renderer.DefaultTimeScaleRenderer;

public class PlanningTimeScaleRenderer extends DefaultTimeScaleRenderer{
    protected void initIterators() {
        _iterators = new ArrayList<DateIterator>();
        _formats = new ArrayList<DateIterator.Format>();
        DateFormatter _formatter = new DateFormatter();
        DateIterator iterator = new MillisecondIterator(1);
        iterator.setFormatter(_formatter);
        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);

        iterator = new MillisecondIterator(10);
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);

        iterator = new MillisecondIterator(100);
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);

        iterator = new MillisecondIterator(500);
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);

        iterator = new SecondIterator(1);
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);

        iterator = new SecondIterator(5);
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);

        iterator = new SecondIterator(30);
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);

        iterator = new MinuteIterator(1);
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);

        iterator = new MinuteIterator(10);
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);
        _upperMap.put(iterator, new DayIterator(1));

        iterator = new MinuteIterator(30);
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);
        _upperMap.put(iterator, new DayIterator(1));

        iterator = new HourIterator(3);
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);
        _upperMap.put(iterator, new DayIterator(1));

        iterator = new HourIterator(12);
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);
        _upperMap.put(iterator, new DayIterator(1));

        iterator = new DayIterator();
        iterator.setFormatter(_formatter);

 
        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);

        iterator = new WeekIterator();
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);
        _upperMap.put(iterator, new MonthIterator());

        iterator = new MonthIterator();
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);
        _upperMap.put(iterator, new YearIterator());

        iterator = new YearIterator();
        iterator.setFormatter(_formatter);

        _iterators.add(iterator);
        _formats.add(DateIterator.Format.LONG);
    }

}
