import math
from itertools import permutations
import csv
import pandas
import numpy as np
from docplex.mp.model import Model
from doopl.factory import *

trajet_1 = np.array([(0, 1, 0), (0, 2, 0), (0, 3, 0), (0, 4, 0), (0, 5, 0), (0, 6, 0), (0, 7, 0)], dtype=object)
trajet_2 = np.array([(0, 4, 0), (0, 1, 2, 0), (0, 3, 7, 0), (0, 5, 6, 0)], dtype=object)


# for i in range(8):
#     for j in range(i + 1, 8):
#         print(round(np.linalg.norm(pos[i] - pos[j])))


# def calcul_cout(trajets):
#     sum = 0
#     for t in trajets:
#         tmp_size = len(t)
#         traj_cost = 0
#         for i in range(tmp_size - 2):
#             sum_p = 0
#             for j in range(i + 1, tmp_size - 1):
#                 sum_p += poids[t[j]]
#
#             traj_cost += np.linalg.norm(pos[t[i]] - pos[t[i + 1]]) * (1628 / (100 - 5 * sum_p))
#
#         traj_cost += np.linalg.norm(pos[t[tmp_size - 2]] - pos[0]) * (1628 / 110)
#
#         sum += traj_cost
#     return sum


def route_cost(route, pos_cli, pos_wh, closer_wh, poids):
    tmp_size = len(route)
    traj_cost = 0
    p_tot = 0
    for i in range(1, tmp_size - 1):
        p_tot += poids[route[i] - 1]
    if p_tot > 5:
        return (False, 0)
    else:
        for i in range(tmp_size - 2):
            sum_p = 0
            for j in range(i + 1, tmp_size - 1):
                sum_p += poids[route[j] - 1]
            prev_pos = pos_wh[int(closer_wh[route[i + 1] - 1])] if i == 0 else pos_cli[route[i] - 1]
            next_pos = pos_cli[route[i + 1] - 1]

            traj_cost += np.linalg.norm(prev_pos - next_pos) * (1628 / (100 - 5 * sum_p))

        traj_cost += np.linalg.norm(
            pos_cli[route[tmp_size - 2] - 1] - pos_wh[int(closer_wh[route[tmp_size - 2] - 1])]) * (1628 / 110)
        traj_cost /= 15
        return traj_cost <= 1628, traj_cost


# calcul_cout(trajet_1)
# calcul_cout(trajet_2)

# pos = np.zeros((20, 2))
# pos[:, 0] = np.random.randint(0, 51, 20)
# pos[:, 1] = np.random.randint(0, 51, 20)
# pos = np.concatenate((np.array([(23, 30)]), pos))
# poids = np.concatenate((np.array([0]),np.random.randint(1, 6, 20)))

file_output = open("DroneDeliveryProblem\ddp_test.dat", "w")
file = open("../Data5.csv", mode="r")
n_client = int(file.readline().replace("\n", "").replace("\"", ""))
n_entrepot = int(file.readline().replace("\n", "").replace("\"", ""))

file_output.write("n_client = " + str(n_client) + ";\n")

pos_warehouse = np.zeros((n_entrepot, 2))
pos_clients = np.zeros((n_client, 2))
poids = np.zeros(n_client)
closer_warehouse = np.zeros(n_client)

iter = 0
for i in range(n_entrepot):
    line = file.readline().replace("\n", "").replace("\"", "").split(",")
    pos_warehouse[iter, :] = np.array((int(line[0]), int(line[1])))
    iter += 1

iter = 0

for i in range(n_client):
    line = file.readline().replace("\n", "").replace("\"", "").split(",")
    pos_clients[iter, :] = np.array((int(line[0]), int(line[1])))
    iter += 1

iter = 0

for i in range(n_client):
    line = file.readline().replace("\n", "").replace("\"", "")
    poids[iter] = float(line)
    iter += 1
# pos = np.array([(23, 30), (15, 2), (32, 3), (42, 36), (48, 20), (10, 40), (3, 33), (22, 55)])
# poids = np.array([0, 1, 3, 4, 2, 2, 2, 1])

for i in range(n_client):
    dist = -1
    best = -1
    for j in range(n_entrepot):
        tmp_dist = math.sqrt(
            math.pow(pos_clients[i, 0] - pos_warehouse[j, 0], 2) + math.pow(pos_clients[i, 1] - pos_warehouse[j, 1], 2))
        if (best == -1 or tmp_dist < dist):
            dist = tmp_dist
            best = j
    closer_warehouse[i] = best

id = 0

list_subtours = []

for i in range(n_client):
    (valid, cost) = route_cost(np.array([0, i + 1, 0]), pos_clients, pos_warehouse, closer_warehouse, poids)
    print("<{0, " + str(i + 1) + ", 0}, " + str(round(cost)) + ">")
    list_subtours.append("<{0, " + str(i + 1) + ", 0}, " + str(round(cost)) + ">\n")
    id += 1
for i in range(n_client):
    for j in range(i + 1, n_client):
        if j != i:
            best_cost = -1
            best_comb = []
            for c in permutations([i, j], 2):
                (valid, cost) = route_cost(np.array([0, c[0] + 1, c[1] + 1, 0]), pos_clients, pos_warehouse,
                                           closer_warehouse, poids)
                if valid and (cost < best_cost or best_cost == -1):
                    best_cost = cost
                    best_comb = c
            if best_cost != -1:
                print("<{0, " + str(best_comb[0] + 1) + ", " + str(best_comb[1] + 1) + ", 0}, " + str(
                    round(best_cost)) + ">")
                list_subtours.append("<{0, " + str(best_comb[0] + 1) + ", " + str(best_comb[1] + 1) + ", 0}, " + str(
                    round(best_cost)) + ">\n")
                id += 1

for i in range(n_client):
    for j in range(i + 1, n_client):
        for k in range(j + 1, n_client):
            best_cost = -1
            best_comb = []
            for c in permutations([i, j, k], 3):
                (valid, cost) = route_cost(np.array([0, c[0] + 1, c[1] + 1, c[2] + 1, 0]), pos_clients, pos_warehouse,
                                           closer_warehouse, poids)
                if valid and (cost < best_cost or best_cost == -1):
                    best_cost = cost
                    best_comb = c
            if best_cost != -1:
                print(
                    "<{0, " + str(best_comb[0] + 1) + ", " + str(best_comb[1] + 1) + ", " + str(
                        best_comb[2] + 1) + ", 0}, " + str(
                        round(best_cost)) + ">")
                list_subtours.append("<{0, " + str(best_comb[0] + 1) + ", " + str(best_comb[1] + 1) + ", " + str(
                    best_comb[2] + 1) + ", 0}, " + str(
                    round(best_cost)) + ">\n")
                id += 1
            # if j != i and j != k and i != k:
            #     (valid, cost) = route_cost(np.array([0, i, j, k, 0]), pos, poids)
            #     if valid:
            #         print("<{0," + str(i) + ", " + str(j) + ", " + str(k) + ",0}, " + str(cost) + ">")
            #         id += 1

print(id)
file_output.write("n_route = " + str(id) + ";\n\n")

file_output.write("subtours = [\n")
for e in list_subtours:
    file_output.write(e)

file_output.write("];\n\n")

file_output.close()
with create_opl_model(model="DroneDeliveryProblem/ddp.mod") as opl:
    # tuple can be a list of tuples, a pandas dataframe...
    opl.set_input("buses", Buses)
    opl.

    # Generate the problem and solve it.
    opl.run()

    # Get the names of post processing tables
    print("Table names are: " + str(opl.output_table_names))

    # Get all the post processing tables as dataframes.
    for name, table in iteritems(opl.report):
        print("Table : " + name)
        for t in table.itertuples(index=False):
            print(t)

        # nicer display
        for t in table.itertuples(index=False):
            print(t[0], " buses ", t[1], "seats")

#
# trajet_3 = np.array([(0, 3, 0), (0, 4, 0), (0, 2, 1, 0), (0, 6, 5, 7, 0)], dtype=object)
# # calcul_cout(trajet_3)
#
# sum_1 = 0
# sum_2 = 0
# for t in trajet_3:
#     print (t)
#     print((route_cost(t, pos, poids))[1])
#     print((calcul_cout(np.array([t]))))
#     sum_1 += (route_cost(t, pos, poids))[1]
#     sum_2 += (calcul_cout(np.array([t])))
#
# print(sum_1)
# print(sum_2)
# print(calcul_cout(trajet_2))
