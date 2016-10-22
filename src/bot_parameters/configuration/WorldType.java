package bot_parameters.configuration;

public enum WorldType {
    F2P (301, 308, 316, 326, 335, 381, 382, 383, 384, 385, 393, 394),
    MEMBERS (302, 303, 304, 305, 306, 309, 310, 311, 312, 313, 314, 317, 318, 319, 320,
            321, 322, 327, 328, 329, 330, 333, 334, 336, 338, 341, 342, 343, 344,
            346, 349, 350, 351, 352, 353, 354, 357, 358, 359, 360, 361, 362, 365,
            366, 367, 368, 369, 370, 373, 375, 376, 377, 386),
    PVP (325, 337),
    DEADMAN (345, 374, 378);

    public final Integer[] worlds;

    WorldType(final Integer... worlds) {
        this.worlds= worlds;
    }
}
