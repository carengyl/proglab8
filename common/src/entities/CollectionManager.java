package entities;

import java.io.Serializable;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Collection data structure
 *
 * @author carengyl
 */
public class CollectionManager implements Serializable {
    private final ReentrantLock reentrantLock = new ReentrantLock();
    /**
     * File name field
     */
    private Path fileName;
    /**
     * Initialization date field
     */
    private final LocalDate initDate;
    /**
     * Hashmap of {@code HumanBeing}
     */

    private HashMap<Long, HumanBeing> humanBeings;

    public CollectionManager() {
        initDate = LocalDate.now();
        humanBeings = new HashMap<>();
    }

    /**
     * Constructs new instance with given path
     *
     * @param fileName path to file
     */
    public CollectionManager(Path fileName) {
        this.fileName = fileName;
        humanBeings = new HashMap<>();
        initDate = LocalDate.now();
    }

    /**
     * Constructs new instance with given path and existing hashmap
     *
     * @param fileName path to file
     * @param humanBeings parsed {@code Hashmap<Long, HumanBeing>}
     */
    public CollectionManager(Path fileName, HashMap<Long,HumanBeing> humanBeings) {
        this.fileName = fileName;
        this.initDate = LocalDate.now();
        this.humanBeings = humanBeings;
        HumanBeing.setCurrentId(getMaxID());
    }

    public void setHumanBeings(HashMap<Long, HumanBeing> humanBeings) {
        try {
            reentrantLock.lock();
            this.humanBeings = humanBeings;
        } finally {
            reentrantLock.unlock();
        }
    }
    /**
     * @return max ID of {@code HumanBeing} from collection
     */
    private long getMaxID() {
        long maxId = 1;
        for (long key: humanBeings.keySet()) {
            if (humanBeings.get(key).getId() > maxId) {
                maxId = humanBeings.get(key).getId();
            }
        }
        return maxId;
    }

    public List<Long> returnIDsOFGreater(HumanBeing humanBeing) {
        try {
            reentrantLock.lock();
            List<Long> ids = new ArrayList<>();
            for (HumanBeing h: humanBeings.values()) {
                if (humanBeing.compareTo(h) < 0) {
                    ids.add(h.getId());
                }
            }
            return ids;
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * @return path to file
     */
    public Path getFileName() {
        return fileName;
    }

    /**
     * @param fileName new File name
     */
    public void setFileName(Path fileName) {
        this.fileName = fileName;
    }

    /**
     * @return Initialization date
     */
    public String getInitDate() {
        return initDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    /**
     * @param id given id
     * @return true if given id is in collection
     */
    public boolean checkForId(long id) {
        try {
            reentrantLock.lock();
            for (long key : humanBeings.keySet()) {
                if (humanBeings.get(key).getId().equals(id)) {
                    return true;
                }
            }
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * Adds new {@code HumanBeing} to collection
     *
     * @param key {@code long} key
     * @param humanBeing parsed {@code HumanBeing}
     */
    public void addByKey(long key, HumanBeing humanBeing) {
        try {
            reentrantLock.lock();
            humanBeings.put(key, humanBeing);
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * @return collection
     */
    public HashMap<Long, HumanBeing> getHumanBeings() {
        try {
            reentrantLock.lock();
            return humanBeings;
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * Replaces {@code HumanBeing} by given id with new {@code HumanBeing}
     *
     * @param id id of old HumanBeing
     * @param humanBeing new parsed HumanBeing
     */
    public void updateById(long id, HumanBeing humanBeing) {
        try {
            reentrantLock.lock();
            for (long key: humanBeings.keySet()) {
                if (Objects.equals(humanBeings.get(key).getId(), id)) {
                    humanBeings.replace(key, humanBeing);
                    return;
                }
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public void removeById(Long id) {
        Set<Long> keysCopy = Set.copyOf(humanBeings.keySet());
        for(long key: keysCopy) {
            if (humanBeings.get(key).getId().equals(id)) {
                humanBeings.remove(key);
                break;
            }
        }
    }

    /**
     * Removes all {@code HumanBeing} from collection, if their key is lower than given
     *
     * @param greaterKey given key
     */
    public String removeLowerKey(long greaterKey) {
        Set<Long> keysCopy = Set.copyOf(humanBeings.keySet());
        StringBuilder stringBuilder = new StringBuilder();
        for(long key: keysCopy) {
            if (key < greaterKey) {
                stringBuilder.append(removeByKey(key)).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public Set<HumanBeing> getUsersElements(List<Long> ids) {
        try {
            reentrantLock.lock();
            Set<HumanBeing> result = new HashSet<>();
            for (HumanBeing humanBeing : humanBeings.values()) {
                if (ids.contains(humanBeing.getId())) {
                    result.add(humanBeing);
                }
            }
            return result.isEmpty() ? null : result;
        } finally {
            reentrantLock.unlock();
        }
    }

    public Set<HumanBeing> getAlienElements(List<Long> ids) {
        try {
            reentrantLock.lock();
            Set<HumanBeing> result = new HashSet<>();
            for (HumanBeing humanBeing : humanBeings.values()) {
                if (!ids.contains(humanBeing.getId())) {
                    result.add(humanBeing);
                }
            }
            return result.isEmpty() ? null : result;
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * Removes instance from collection by given key
     *
     * @param key given key
     */
    public String removeByKey(long key) {
        humanBeings.remove(key);
        return "Deleted element by key:" + key;
    }

    public String returnInfo() {
        try {
            reentrantLock.lock();
            return "Collection type: " + humanBeings.getClass().toString().substring(6) +
                    "; type of elements: "+ HumanBeing.class.toString().substring(6) +
                    "; initialized: " + initDate +
                    "; number of elements: " + humanBeings.size();
        } finally {
            reentrantLock.unlock();
        }
    }
}
