package com.jsf2184.dnb;

import com.jsf2184.dnb.utility.TimeMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class LogEntryWindow {

    String timestamp;
    String className;
    String message;
    LinkedList<LogEntry> previousLogs;

    public LogEntryWindow() {
        previousLogs = new LinkedList<>();
        timestamp = null;
    }

    LogEntry getCurrent() {
        if (timestamp == null) {
            return null;
        }
        LogEntry current = new LogEntry(TimeMapper.toDateSeconds(timestamp),
                                        className,
                                        message);
        return current;
    }

    public void add(LogEntry logEntry) {
        LogEntry current = getCurrent();
        final long newTime = logEntry.getTimestamp();
        long predecessor = current == null ? 0 : current.getTimestamp();
        if (newTime < predecessor) {
            // log is not in increasing order, throw this entry away
            log.warn("Disregarding out-or-order log entry, newest timestamp of {} is less than predecessor timestamp of {}",
                     TimeMapper.toDateString(newTime), TimeMapper.toDateString(current.getTimestamp()));
            return;
        }
        if (current != null) {
            previousLogs.addLast(current);
        }
        timestamp = logEntry.getTimestampString();
        className = logEntry.getClassName();
        message = logEntry.getMessage();

        long earliestAlllowed = newTime - 5;
        while (!previousLogs.isEmpty()) {
            final LogEntry first = previousLogs.peekFirst();
            if (first.getTimestamp() < earliestAlllowed) {
                previousLogs.removeFirst();
            } else {
                break;
            }
        }
    }

    int size() {
        return (getCurrent() == null ? 0 : 1) +  previousLogs.size();
    }

    List<Long> getTimestamps() {
        final LogEntry current = getCurrent();
        final List<Long> result = previousLogs.stream().map(LogEntry::getTimestamp).collect(Collectors.toList());
        if (current != null) {
            result.add(current.getTimestamp());
        }
        return result;
    }

}
