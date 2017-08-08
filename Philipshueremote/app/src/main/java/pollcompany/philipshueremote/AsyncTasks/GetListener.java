package pollcompany.philipshueremote.AsyncTasks;

import java.util.List;

import pollcompany.philipshueremote.Lamp;

/**
 * Created by Ian on 22-7-2017.
 */

public interface GetListener {
    void updateContent(List items);
    void notReachable();
}