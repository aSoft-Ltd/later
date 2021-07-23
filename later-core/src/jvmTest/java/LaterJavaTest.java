import later.Later;
import org.junit.jupiter.api.Test;

public class LaterJavaTest {
    private void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {

        }
    }

    private Later<Integer> later(Integer value) {
        return new Later<>((resolve, reject) -> {
            System.out.println("Sleeping");
            sleep(2000);
            System.out.println("finished sleeping");
            if (value < 5) reject.invoke(new Exception("Number (" + value + ") is less than 5"));
            resolve.invoke(value);
        });
    }

    @Test
    public void should_hav_a_goo_api() {
        later(6).then(res -> {
            System.out.println("Result 1: " + (res + 1));
            sleep(2000);
            return res + 1;
        }).then(res -> {
            System.out.println("Result 2: " + (res + 1));
            sleep(2000);
            return res + 1;
        }).complete(res -> {
            System.out.println("Finished with " + res);
        }).wait();
    }

    @Test
    public void should_catch_and_recover_accordingly() {
        later(4).error(err -> {
            System.out.println("Found Error 1: " + err);
            sleep(2000);
            return 1;
        }).then(res -> {
            System.out.println("Result 1: " + (res + 1));
            sleep(2000);
            return res + 1;
        }).then(res -> {
            System.out.println("Result 2: " + (res + 1));
            sleep(2000);
            return res + 1;
        }).complete(res -> {
            System.out.println("Finished with " + res);
        }).wait();
    }

    @Test
    public void should_map_future_values() {
        later(4).error(err -> {
            System.out.println("Found Error 1: " + err);
            sleep(2000);
            return "A";
        }).then(res -> {
            System.out.println("Result 1: " + res + 1);
            sleep(2000);
            return res + 1;
        }).then(res -> {
            System.out.println("Result 2: " + res + 1);
            sleep(2000);
            return res + 1;
        }).complete(res -> {
            System.out.println("Finished with " + res);
        }).wait();
    }
}
