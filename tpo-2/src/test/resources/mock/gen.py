import math


xs = [2.64073, 1.75607, 1.28947, 0.77633, 0.31433, 0.23113, -0.19014, -0.372335, -0.56731, -0.798823, -1.0022, -1.962474, -2.088058, -2.112865, -2.126812, -2.712572, -3.48133, -4.84266, -4.96331, -5.13331, -5.42525, -5.62233, -6.06165, 1.76094, 1.94313, 2.13811, 2.36962, 2.573, 3.53327, 3.65885, 3.68366, 3.69761, 4.28337, 5.05213, 6.41346, 6.53411, 6.70411, 6.99605, 7.19313, 7.63245, 2.0, 3.0, 5.0, 10.0]

fun = {
    "sin": math.sin,
    "cos": math.cos,
    "tan": math.tan,
    "ln": lambda x: math.log(x),
    "sec": lambda x: 1 / math.cos(x),
    "csc": lambda x: 1 / math.sin(x),
}

base_dir = "/home/petr/study/tpo/tpo-2/src/test/resources/mock"

for n, f in fun.items():
    with open(f"{base_dir}/{n}.csv", "w") as out:
        res = []
        for x in xs:
            try:
                res.append((str(x), str(f(x))))
            except:
                pass
        out.write('\n'.join([','.join(row) for row in res]))

with open(f"{base_dir}/log.csv", "w") as out:
    res = []
    for base in [2.0, 3.0, 5.0, 10.0]:
        for x in xs:
            try:
                res.append((base, x, math.log(x, base)))
            except:
                pass
    out.write('\n'.join([','.join([str(j) for j in row]) for row in res]))
