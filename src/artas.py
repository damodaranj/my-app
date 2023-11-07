
# Comment for Khaleel
# Comment #3 for Khaleel
import subprocess
import argparse
from typing import Counter
import pandas as pd
import csv
import zipfile
import glob
from PIL import Image
import pytesseract
import wget
from pdf2image import convert_from_path
from datetime import date, datetime
import os
import re
import json
import sys
import time
import requests
import contextlib

from requests.api import options

# Random commit


def colorRed(text): print("\033[91m{}\033[00m" .format(text))
def colorGreen(text): print("\033[92m{}\033[00m" .format(text))
def colorOrange(text): print("\033[93m{}\033[00m" .format(text))
def colorLightPurple(text): print("\033[94m{}\033[00m" .format(text))
def colorPurple(text): print("\033[95m{}\033[00m" .format(text))
def colorCyan(text): print("\033[96m{}\033[00m" .format(text))
def colorLightGray(text): print("\033[97m{}\033[00m" .format(text))
def colorBlack(text): print("\033[98m{}\033[00m" .format(text))


cwd = os.getcwd()
PDF_file = "report.pdf"
PRF_file = "report.csv"
current_time = time.strftime("%Y%m%d")
outfile = 'CRF-%s-' % PDF_file + current_time + '.csv'
scanfile = 'scanfile'
procfile = 'procfile'
newoutfile = 'Prioritized-%s.csv' % PRF_file
master_report_record = 'Master-Report-Record.csv'

project_name = 'ERROR'
beginning = 0
end1 = 42069
report_vendor = 'd'
verbose_mode = 0


def bordered(text):
    lines = text.splitlines()
    width = max(len(s) for s in lines)
    if width % 2 == 0:
        new_width = int(((width / 2) - 8))
    else:
        width = int(width) + 1
        new_width = int((width / 2) - 8)
    res = ['┌' + '─' * new_width + " [ FILE READY ] " + '─' * new_width + '┐']
    for s in lines:
        res.append('│' + (s + ' ' * width)[:width] + '│')
    res.append('└' + '─' * width + '┘')
    return '\n'.join(res)


def extraction():

    global verbose_mode

    if verbose_mode == 1:
        colorCyan("[OPERATION STARTED] - CRF ASSESSMENT")

    global outfile
    global project_name
    cwd = os.getcwd()
    global scanfile
    global procfile
    global current_time
    global report_vendor

    if verbose_mode == 1:
        print("Purging and deleting irrelevant lines")

    if report_vendor == 'w':
        purge_words = ['intended solely for the information', 'Larsen & Toubro', 'Simplifying Enterprise Security!', '', 'in any manner, or', 'WeSecureApp', '®', 'WESECUREAPP',
                       'Nagpur Network', 'Network Vulnerability Assessment Report', '©', 'All Rights Reserved', '22 Jun, 2020 © WeSecureApp. All Rights Reserved 2020.', 'AFFECTED ASSET[S]']
        delete_list = ["CVE-Reference", "CVE - Reference", "Vulnerable Server",
                       "Vulnerable Servers", "CVE - Reference;", "CVE-Reference;", " | "]

    elif report_vendor == 'd':
        purge_words = ['intended solely for the information', 'Larsen & Toubro',
                       'Deloitte', '', 'in any manner, or', 'Assessment Draft Report', '®']
        delete_list = ["CVE-Reference", "CVE - Reference", "Vulnerable Server",
                       "Vulnerable Servers", "CVE - Reference;", "CVE-Reference;"]

    with open(scanfile) as oldfile, open(procfile, 'w') as newfile:
        for line in oldfile:
            if not any(purge_word in line for purge_word in purge_words):
                for word in delete_list:
                    line = line.replace(word, "")
                if re.search(r"(#\d+)", line):
                    line = line.replace(r"(#\d+)", "")
                line = line.lstrip()
                newfile.write(line)

    op_list = []
    res = []
    ip_res = []
    flag = 0
    scope = 0
    exploit_infos = []
    advisories = []
    location = 0
    cve_res = []
    search_string_one = 'advisories'
    search_string_two = 'Advisory'
    search_string_three = 'Patch'
    search_string_four = 'SECTRACK'
    search_string_five = 'Government'
    date_res = []
    likelyhood = 0
    cvecount = 0
    artas_base_score = 0
    exp_info_str = ''
    adv_found = 0
    adv_str = ''
    severity = 0
    cvss_base_score = 0
    artas_risk_factor = 0

    regex_d = r"^(3\.1\.\d+)(.*)"
    regex_w = r"AFFECTED NODES ADDITIONAL INFORMATION"
    enc_pattern = r"^[^a-zA-Z]*"

    with open(procfile) as oldfile:
        data = {'Vulnerability Name': '', 'IP Address': [], 'Asset Count': '', 'CVE Reference': [], 'Exploit ID': [],
                'Exploit Creation Date': [], 'Likelyhood': '', 'Internet Facing (Y/N)': '', 'Asset Criticality (H/M/L)': '', 'Risk Score': '', 'Remediation Priority': '', 'Month Reported': '', 'Report Status': '', 'Month Closed': '', 'Closed By': '', 'Advisories': [], 'Project Name': ''}
        line = next(oldfile)
        line = line.strip()
        if verbose_mode == 1:
            print("Fetching CVE and Exploit IDs.")
        while True:
            previousline2 = line
            try:
                line = next(oldfile)
                previousline1 = line
                line = next(oldfile)
                line = line.strip()
            except StopIteration:
                break
            if report_vendor == 'd':
                regex = regex_d
            else:
                regex = regex_w
            if re.search(regex, line):
                if data['Vulnerability Name']:
                    ip_str_temp = (", ".join(str(e) for e in ip_res))
                    data['IP Address'].append(ip_str_temp)
                    data['IP Address'] = ','.join(data['IP Address'])
                    cve_str_temp = (", ".join(str(e) for e in cve_res))
                    data['CVE Reference'].append(cve_str_temp)
                    data['CVE Reference'] = ','.join(data['CVE Reference'])
                    data['Asset Count'] = len(data['IP Address'].split(','))
                    listToStr = ','.join([str(elem) for elem in res])
                    data['Exploit ID'].append(listToStr)
                    data['Exploit ID'] = ','.join(data['Exploit ID'])
                    dp_str = ','.join([str(elem) for elem in date_res])
                    data['Exploit Creation Date'].append(dp_str)
                    data['Exploit Creation Date'] = ','.join(
                        data['Exploit Creation Date'])
                    # data['ARTAS Likelyhood Score'] = likelyhood
                    # data['ARTAS CIA Score'] = severity
                    data['Likelyhood'] = round(
                        artas_base_score, 2)
                    # data['CVSS Base Score'] = cvss_base_score
                    # expinfo_str_temp = (", ".join(str(e)
                    #                     for e in exploit_infos))
                    # data['Exploit Information'].append(expinfo_str_temp)
                    # data['Exploit Information'] = ','.join(
                    #     data['Exploit Information'])
                    adv_str_temp = (", ".join(str(e) for e in advisories))
                    data['Advisories'].append(adv_str_temp)
                    data['Advisories'] = ','.join(data['Advisories'])
                    data['Project Name'] = project_name
                    op_list.append(data)
                    res = []
                    cvecount = 0
                    cvss_base_score = 0
                    current_risk_factor = 0
                    artas_base_score = 0
                    cvecount = 0
                    listToStr = ''
                    location = 0
                    flag = 0
                    artas_risk_factor = 0
                    adv_found = 0
                    severity = 0
                    scope = 0
                    exploit_infos = []
                    exp_info_str = ''
                    adv_str = ''
                    advisories = []
                    likelyhood = 0
                    dp_str = ''
                    ip_res = []
                    date_res = []
                    cve_res = []
                    data = {'Vulnerability Name': '', 'IP Address': [], 'Asset Count': '', 'CVE Reference': [], 'Exploit ID': [],
                            'Exploit Creation Date': [], 'Likelyhood': '', 'Internet Facing (Y/N)': '', 'Asset Criticality (H/M/L)': '', 'Risk Score': '', 'Remediation Priority': '', 'Month Reported': '', 'Report Status': '', 'Month Closed': '', 'Closed By': '', 'Advisories': [], 'Project Name': ''}
                if report_vendor == 'w':
                    previousline1 = previousline1.strip()
                    if 'ASSET' in previousline1:
                        if 'ASSET' not in previousline2:
                            previousline2 = previousline2.strip()
                            tempprevline = re.sub(enc_pattern, "", previousline2.encode(
                                "ascii", errors="ignore").decode())
                            data['Vulnerability Name'] = tempprevline
                    else:
                        tempprevline = re.sub(enc_pattern, "", previousline1.encode(
                            "ascii", errors="ignore").decode())
                        data['Vulnerability Name'] = tempprevline
                else:
                    data['Vulnerability Name'] = re.match(
                        r"^(3\.1\.+\d+)(.*)", line.encode("ascii", errors="ignore").decode()).group(2)
            if re.search(r"\b(?:(?:2(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\.){"
                         r"3}(?:(?:2([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9]))\b", line):
                ip_pattern = re.compile(
                    r'\b(?:(?:2(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\.){3}(?:(?:2([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9]))\b')
                for ip_m in re.finditer(ip_pattern, line):
                    ip_temp = re.findall(r'\d+.\d+.\d+.\d+', ip_m.group(0))
                    if ip_temp not in ip_res:
                        ip_res.append(ip_temp)
            if re.search(r"(?:CVE-)", line):
                cve_pattern = re.compile(r"(CVE-)(\d+-\d+)")
                for cve_m in re.finditer(cve_pattern, line):
                    cve_temp = re.findall(r'\d+-\d+', cve_m.group(0))
                    if cve_temp not in cve_res:
                        cve_res.append(cve_temp)
                    cve_temp_str = (", ".join(str(e) for e in cve_temp))
                    cve_temp_str_year = cve_temp_str.split('-')[0]
                    overall_cve_str = 'CVE-' + cve_temp_str
                    if int(cve_temp_str_year) < 2002:
                        dbfile = 'nvdcve-1.1-2002.json'
                    else:
                        dbfile = 'nvdcve-1.1-' + \
                            str(cve_temp_str_year) + '.json'
                    cvecount = cvecount + 1
                    location = 0
                    try:
                        os.chdir("Data Feeds")
                        if verbose_mode == 1:
                            print("Loading JSON File: " + str(dbfile))
                            time.sleep(0.2)
                        with open(dbfile) as f:
                            cveapirespdata = json.load(f)
                        if verbose_mode == 1:
                            print("Success Loading JSON File")
                            time.sleep(0.2)
                    except:
                        colorRed("[CRITICAL ERROR] - Unable to load JSON File")
                        if verbose_mode == 1:
                            time.sleep(0.2)
                            colorRed("Data Feed for: " + str(dbfile))
                            time.sleep(0.2)
                            colorRed("Current directory contains: ",
                                     os.listdir('.'))
                        exit()
                    if verbose_mode == 1:
                        time.sleep(0.2)
                        print("JSON Dump for CVE-" + cve_temp_str)
                        time.sleep(0.2)
                        colorOrange(cveapirespdata)
                        time.sleep(0.2)
                        print('\n\n')
                    adv_found = 0
                    os.chdir(cwd)
                    if verbose_mode == 1:
                        time.sleep(0.2)
                        print("Trying to locate the CVE Location in the JSON File")
                        print("Checking for CVE ID: " + str(overall_cve_str))
                    for n in range(len(cveapirespdata['CVE_Items'])):
                        if verbose_mode == 1:
                            time.sleep(0.001)
                            colorGreen("Index Location: " + str(n))
                            print("CVE ID at Index Location: ", str(
                                cveapirespdata['CVE_Items'][n]['cve']['CVE_data_meta']['ID']))
                        if overall_cve_str in cveapirespdata['CVE_Items'][n]['cve']['CVE_data_meta']['ID']:
                            location = n
                            if verbose_mode == 1:
                                time.sleep(0.1)
                                colorCyan(
                                    "CVE ID Matched at Index Location: " + str(location))
                                colorOrange("CVE ID Matched at Index: " + str(
                                    cveapirespdata['CVE_Items'][n]['cve']['CVE_data_meta']['ID']))
                                print("CVE ID Required: " +
                                      str(overall_cve_str))
                                colorGreen("CVE Match successful!")
                                print("Ignoring other Index Locations.")
                                print("\n\n")
                                time.sleep(0.5)
                            break
                    try:
                        if verbose_mode == 1:
                            colorCyan(
                                "[CVE PROGRESS] Trying to fetch CVSS 3.1 information for CVE: " + cve_temp_str)
                            time.sleep(0.1)
                            colorGreen("Now at Index: " + str(location))
                            time.sleep(0.1)
                        vector_string = json.dumps(
                            cveapirespdata['CVE_Items'][location]['impact']['baseMetricV3']['cvssV3']['vectorString'])
                        cvss_base_score = json.dumps(
                            cveapirespdata['CVE_Items'][location]['impact']['baseMetricV3']['cvssV3']['baseScore'])
                        exp_info_str = json.dumps(
                            cveapirespdata['CVE_Items'][location]['cve']['description']['description_data'][0]['value'])
                        if exp_info_str not in exploit_infos:
                            exploit_infos.append(exp_info_str)
                        for t in cveapirespdata['CVE_Items'][location]['cve']['references']['reference_data']:
                            for n in range(len(t['tags'])):
                                if search_string_five in t['tags'][n]:
                                    adv_str = t['url']
                                    adv_found = 1
                                elif search_string_three in t['tags'][n] and (adv_found == 0):
                                    adv_str = t['url']
                                    adv_found = 1
                                elif search_string_two in t['tags'][n] and (adv_found == 0):
                                    adv_str = t['url']
                                    adv_found = 1
                            if search_string_one in t['url'] and (adv_found == 0):
                                adv_str = t['url']
                                adv_found = 1
                            elif search_string_four in t['refsource'] and (adv_found == 0):
                                adv_str = t['url']
                                adv_found = 1
                        if adv_found == 0:
                            adv_found = 1
                            adv_str = t['url']
                        if adv_str not in advisories:
                            advisories.append(adv_str)

                        result = re.search(
                            r"CVSS:3\.\d/AV:([A-Z])/AC:([A-Z])/PR:([A-Z])/UI:([A-Z])/S:([A-Z])/C:([A-Z])/I:([A-Z])/A:([A-Z])", vector_string)

                        # G1 - AV - N/L/A/P
                        # G2 - AC - L/H
                        # G3 - PR - N/L/H
                        # G4 - UI - N/R
                        # G5 - S - C/U
                        # G6 - C - N/L/H
                        # G7 - I - N/L/H
                        # G8 - A - N/L/H

                        cia = {
                            'N': 0,
                            'L': 3,
                            'H': 7
                        }
                        severity = (cia.get(result.group(
                            6)) + cia.get(result.group(7)) + cia.get(result.group(8))) / 3
                        if result.group(1) == 'L':
                            like_et = 3
                        elif result.group(1) == 'A':
                            like_et = 3
                        elif result.group(1) == 'N':
                            like_et = 7
                        elif result.group(1) == 'P':
                            like_et = 3
                        if result.group(2) == 'L':
                            like_ee = 3
                        elif result.group(2) == 'H':
                            like_ee = 7
                        if result.group(3) == 'H':
                            like_ar = 2
                        elif result.group(3) == 'L':
                            like_ar = 3
                        elif result.group(3) == 'N':
                            like_ar = 5
                        if result.group(4) == 'R':
                            like_ui = 3
                        elif result.group(4) == 'N':
                            like_ui = 7
                        scope = 10
                    except:
                        if verbose_mode == 1:
                            colorOrange(
                                "[CVE ALERT] Unable to fetch CVSS 3.1 information for CVE: " + cve_temp_str)
                            time.sleep(0.2)
                            print(
                                "[CVE ALERT] Trying to fetch CVSS 2.0 information for CVE: " + cve_temp_str)
                            time.sleep(0.2)
                        try:
                            vector_string = json.dumps(
                                cveapirespdata['CVE_Items'][location]['impact']['baseMetricV2']['cvssV2']['vectorString'])
                            cvss_base_score = json.dumps(
                                cveapirespdata['CVE_Items'][location]['impact']['baseMetricV2']['cvssV2']['baseScore'])
                            exp_info_str = json.dumps(
                                cveapirespdata['CVE_Items'][location]['cve']['description']['description_data'][0]['value'])
                            if exp_info_str not in exploit_infos:
                                exploit_infos.append(exp_info_str)
                            for t in cveapirespdata['CVE_Items'][location]['cve']['references']['reference_data']:
                                for n in range(len(t['tags'])):
                                    if search_string_five in t['tags'][n]:
                                        adv_str = t['url']
                                        adv_found = 1
                                    elif search_string_three in t['tags'][n] and (adv_found == 0):
                                        adv_str = t['url']
                                        adv_found = 1
                                    elif search_string_two in t['tags'][n] and (adv_found == 0):
                                        adv_str = t['url']
                                        adv_found = 1
                                if search_string_one in t['url'] and (adv_found == 0):
                                    adv_str = t['url']
                                    adv_found = 1
                                elif search_string_four in t['refsource'] and (adv_found == 0):
                                    adv_str = t['url']
                                    adv_found = 1
                            if adv_found == 0:
                                adv_found = 1
                                adv_str = t['url']
                            if adv_str not in advisories:
                                advisories.append(adv_str)
                            result = re.search(
                                r"AV:([A-Z])/AC:([A-Z])/Au:([A-Z])/C:([A-Z])/I:([A-Z])/A:([A-Z])", vector_string)

                            # G1 - AV - N/L/A
                            # G2 - AC - L/H/M
                            # G3 - Au - N/S/M
                            # G4 - C - N/R
                            # G5 - I - C/U
                            # G6 - A - N/L/H

                            cia = {
                                'N': 0,
                                'P': 3,
                                'C': 7
                            }
                            severity = round((cia.get(result.group(
                                4)) + cia.get(result.group(5)) + cia.get(result.group(6))) / 3, 2)
                            if result.group(1) == 'L':
                                like_et = 3
                            elif result.group(1) == 'A':
                                like_et = 3
                            elif result.group(1) == 'N':
                                like_et = 7
                            if result.group(2) == 'L':
                                like_ee = 5
                            elif result.group(2) == 'M':
                                like_ee = 3
                            elif result.group(2) == 'H':
                                like_ee = 2
                            if result.group(3) == 'M':
                                like_ar = 1
                            elif result.group(3) == 'S':
                                like_ar = 3
                            elif result.group(3) == 'N':
                                like_ar = 6
                            uireq = json.dumps(
                                cveapirespdata['CVE_Items'][0]['impact']['baseMetricV2']['userInteractionRequired'])
                            uiresult = re.search(
                                r'(.*)([Tt][Rr][Uu][Ee]|[Ff][Aa][Ll][Ss][Ee])', uireq)
                            if uiresult.group(2) == "true":
                                like_ui = 3
                            elif uiresult.group(2) == "false":
                                like_ui = 7
                            scope = 10
                        except:
                            flag = -1
                            if verbose_mode == 1:
                                colorRed(
                                    "[CRITICAL ERROR] - Unable to fetch CVSS 2.0 information for CVE: " + cve_temp_str)
                                time.sleep(0.2)
                                print("JSON File Searched: ", dbfile)
                                time.sleep(0.2)
                    try:
                        headers = {
                            'authority': 'www.exploit-db.com',
                            'sec-ch-ua': '^\\^Chromium^\\^;v=^\\^86^\\^, ^\\^^\\^\\^\\^Not^\\^\\^\\^\\A;Brand^\\^;v=^\\^99^\\^,',
                            'Referer': '',
                            'sec-ch-ua-mobile': '?0',
                            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36',
                            'Origin': 'https://www.exploit-db.com',
                            'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36',
                            'content-type': 'text/plain',
                            'accept': 'application/json, text/javascript, */*; q=0.01',
                            'origin': 'https://www.exploit-db.com',
                            'sec-fetch-site': 'same-origin',
                            'sec-fetch-mode': 'cors',
                            'sec-fetch-dest': 'empty',
                            'accept-language': 'en-US,en;q=0.9',
                            'x-requested-with': 'XMLHttpRequest',
                            'referer': 'https://www.exploit-db.com/search?cve=2007-1036',
                        }

                        params = (
                            ('cve', cve_temp_str),
                        )

                        response = requests.get(
                            'https://www.exploit-db.com/search', headers=headers, params=params)

                        if response.status_code == 200:
                            respdata = response.json()
                            respstr = json.dumps(respdata['data'])
                            eid_pattern = re.compile(
                                r'\"exploit_id\": \"[0-9]*\"')
                            date_pattern = re.compile(
                                r'\"date_published\": \"[0-9]+-[0-9]+-[0-9]+\"')
                            for m in re.finditer(eid_pattern, respstr):
                                temp = re.findall(r'\d+', m.group(0))
                                if temp not in res:
                                    res.append(temp)
                            for dp_m in re.finditer(date_pattern, respstr):
                                dp_temp = re.findall(
                                    r'[0-9]+-[0-9]+-[0-9]+', dp_m.group(0))
                                if dp_temp not in date_res:
                                    date_res.append(dp_temp)
                            dt_lead = 0
                            dt_test = 0
                            for dt_i in range(len(date_res)):
                                dt_test = int(date_res[dt_i])
                                if dt_test > dt_lead:
                                    dt_lead = dt_test
                            if (2021 - dt_lead) <= 1:
                                like_ed = 5
                            elif (2021 - dt_lead) <= 3:
                                like_ed = 3
                            else:
                                like_ed = 2
                    except:
                        if verbose_mode == 1:
                            colorOrange(
                                "[EDB ALERT] No Exploit/EID available for CVE: " + cve_temp_str)
                            time.sleep(0.2)
                        like_ed = 1

                    if flag == -1:
                        likelyhood = 0
                        severity = 0
                        artas_base_score = 0
                        cvss_base_score = 0
                        flag = 0

                    else:
                        likelyhood = round(
                            (like_et + like_ee + like_ar + like_ui + like_ed + scope) / 6, 2)

                        artas_base_score = round(
                            (likelyhood + severity) / 2, 2)

                    current_risk_factor = round(artas_base_score, 2)
                    if current_risk_factor >= artas_risk_factor:
                        artas_risk_factor = current_risk_factor
                    else:
                        artas_risk_factor = artas_risk_factor
                    artas_risk_factor = round(artas_risk_factor, 2)

                    if verbose_mode == 1:
                        colorCyan("\nInformation Fetched: ")
                        time.sleep(0.2)
                        print("CVSS Base Score: " + str(cvss_base_score))
                        time.sleep(0.2)
                        print("ARTAS Likelyhood: " + str(likelyhood) + "\nARTAS CIA: " + str(severity) +
                              "\nCurrent Risk Factor: " + str(current_risk_factor) + "\nHighest Risk Factor: " + str(artas_risk_factor))
                        time.sleep(0.2)
                        print("\n")

    pd.DataFrame(op_list).to_csv(outfile, index=False)
    os.rename(outfile, outfile.replace('.pdf', ''))
    if verbose_mode == 1:
        colorOrange(cveapirespdata)
        print("\n")


def initialization():

    global verbose_mode

    if verbose_mode == 1:
        colorCyan("\n\n\n[OPERATION STARTED] - EXTRACTION")

    image_counter = 1
    test = 0
    counter = 0
    global outfile
    global beginning
    global scanfile
    global procfile
    global end1

    with contextlib.suppress(FileNotFoundError):
        os.remove(procfile)
        os.remove(scanfile)

    if verbose_mode == 1:
        print("Readying Pages for Extraction.")
    pages = convert_from_path(PDF_file, 200)

    if verbose_mode == 1:
        print("Checking the number of pages")
    for test2 in pages:
        test = test + 1

    if end1 > test:
        end = test
    elif end1 < test:
        end = end1

    if verbose_mode == 1:
        print("\n\nExtrating Pages.")
    for each_page in pages:
        if counter >= beginning:
            if counter <= end:
                if verbose_mode == 1:
                    print("Converting Page #" + str(counter) + " to JPG")
                filename = "page_"+str(image_counter)+".jpg"
                each_page.save(filename, 'JPEG')
                image_counter = image_counter + 1
        counter = counter + 1

    filelimit = image_counter - 1

    if verbose_mode == 1:
        print("\n\nScanning Pages with OCR.")

    f = open(scanfile, "w")
    for i in range(1, filelimit + 1):
        filename = "page_"+str(i)+".jpg"
        if verbose_mode == 1:
            print("Scanning and removing " + str(filename))
        text = str(((pytesseract.image_to_string(Image.open(filename)))))
        text = text.replace('-\n', '')
        f.write(text)
        os.remove(filename)
    f.close()

    if verbose_mode == 1:
        colorCyan("\n\n\n[OPERATION COMPLETE] - EXTRACTION")
        print(" ")
        colorLightPurple(
            "\nSTAGE [1] COMPLETE. BEGINNING STAGE [2] IN 3 SECONDS. ")

    time.sleep(3)
    extraction()

    if verbose_mode == 1:
        colorCyan("\n\n\n[OPERATION COMPLETE] - CRF ASSESSMENT")
        print(" ")
        os.rename(procfile, procfile + '-' + str(outfile) + '.txt')
        os.rename(scanfile, scanfile + '-' + str(outfile) + '.txt')
        # os.rename(procfile, procfile.replace('.pdf', ''))
        # os.rename(procfile, procfile.replace('.csv', ''))
        # os.rename(scanfile, scanfile.replace('.pdf', ''))
        # os.rename(scanfile, scanfile.replace('.csv', ''))
    else:
        print("Performing Clean-up")
        with contextlib.suppress(FileNotFoundError):
            os.remove(procfile)
            os.remove(scanfile)

    if verbose_mode == 1:
        colorGreen("Generating ARTAS Report")
    # os.rename(outfile, outfile.replace('.pdf', ''))
    print("\n\n")
    colorGreen(bordered("          ARTAS Report - '" +
               str(outfile) + "' is ready for viewing.          "))
    print("\n\n")
    exit()


def prioritization():
    global verbose_mode

    if verbose_mode == 1:
        print(" ")
        colorCyan("[OPERATION STARTED] - REMEDIATION PRIORITIZATION")
        print(" ")

    global PRF_file
    global outfile
    global master_report_record
    current_csv_file = ''
    global newoutfile
    month = datetime.now().strftime('%B %Y')

    row_count = 0
    ac = 0.0
    intface = 0.0
    artas_prf = 0.0
    cvss_prf = 0
    do_now_count = 0
    do_next_count = 0
    do_later_count = 0
    artas_total_prf = 0.0
    cvss_total_prf = 0.0
    artas_score = 0.0
    artas_prf = 0.0
    artas_temp_prf = 0.0
    artas_total_prf = 0.0
    artas_valid_prf = 0
    artas_avg_prf = 0.0
    artas_seper = 0.0
    cvss_score = 0.0
    cvss_prf = 0.0
    cvss_temp_prf = 0.0
    cvss_total_prf = 0.0
    cvss_valid_prf = 0
    cvss_avg_prf = 0.0
    cvss_seper = 0.0

    tempoutfile = 'Temp-%s' % outfile

    # Row[6] = ARTAS Likelyhood
    # Row[7] = Internet Facing
    # Row[8] = Asset Criticality
    # Row[9] = ARTAS PRF
    # Row[10] = ARTAS RP
    # Row[11] = Advisories
    # Row[12] = Exploit Information

    with contextlib.suppress(FileNotFoundError):
        os.remove(tempoutfile)
        os.remove(procfile)
        os.remove(scanfile)

    df = pd.read_csv(PRF_file)

    with open(PRF_file, 'r') as csvfile:
        csvreader = csv.reader(csvfile)
        fields = next(csvreader)
        for row in csvreader:
            if row[7] == 'Y' or row[7] == 'Yes':
                intface = 8
            elif row[7] == 'N' or row[7] == 'No':
                intface = 2
            if row[8] == 'H' or row[8] == 'High':
                ac = 5
            elif row[8] == 'M' or row[8] == 'Medium' or row[8] == 'Med':
                ac = 3
            elif row[8] == 'L' or row[8] == 'Low':
                ac = 2

            artas_score = float(row[6])
            #  artas_likelyhood = float(row[6])
            # artas_likelyhood_prf = round(
            #     (artas_likelyhood + ac + intface) / 3, 2)
            artas_temp_prf = round((ac + intface) / 2, 2)
            artas_prf = round(
                (artas_score + artas_temp_prf) / 2, 2)
            df.loc[row_count, "Risk Score"] = artas_prf
            artas_total_prf = round(artas_total_prf + artas_prf, 2)
            if artas_prf > 0:
                artas_valid_prf = artas_valid_prf + 1

            # cvss_score = float(row[9])
            # cvss_temp_prf = round((ac + intface) / 2, 2)
            # cvss_prf = round(
            #     (cvss_score + cvss_temp_prf) / 2, 2)
            # df.loc[row_count, "CPRF"] = cvss_prf
            # cvss_total_prf = round(cvss_total_prf + cvss_prf, 2)
            # if cvss_prf > 0:
            #     cvss_valid_prf = cvss_valid_prf + 1

            row_count = row_count + 1

            # if verbose_mode == 1:
            #     print(
            #         "\nCalculating Base and PRF Scores for Row[" + str(row_count) + "]")
            #     colorCyan("Data Calculated: ")
            #     print("ARTAS Base Score: " + str(artas_score))
            #     print("ARTAS PRF Score: " + str(artas_prf))
            #     print("CVSS Base Score: " + str(cvss_score))
            #     print("CVSS PRF Score: " + str(cvss_prf))
            #     time.sleep(0.3)

    row_count = 0

    df.to_csv(tempoutfile, index=False)

    dfnew = pd.read_csv(tempoutfile)

    if verbose_mode == 1:
        colorCyan("\n\nCalculating Averages and Medians for PRF")

    artas_avg_prf = round(artas_total_prf / artas_valid_prf, 2)
    temp_artas_avg_prf = artas_avg_prf
    artas_seper = round(artas_avg_prf * 1.2, 2)
    artas_avg_prf = round(artas_avg_prf - (artas_avg_prf * 0.2), 2)

    # cvss_avg_prf = round(cvss_total_prf / cvss_valid_prf, 2)
    # temp_cvss_avg_prf = cvss_avg_prf
    # cvss_seper = round(cvss_avg_prf * 1.2, 2)
    # cvss_avg_prf = round(cvss_avg_prf - (cvss_avg_prf * 0.2), 2)

    if verbose_mode == 1:
        print("Total ARTAS PRF: " + str(artas_total_prf))
        print("Valid ARTAS PRF: " + str(artas_valid_prf))
        print("Average ARTAS PRF: " + str(temp_artas_avg_prf))
        print("ARTAS PRF Seperator: " + str(artas_seper))
        print("Modified Average ARTAS PRF: " + str(artas_avg_prf))

        # print("Total CVSS PRF: " + str(cvss_total_prf))
        # print("Valid CVSS PRF: " + str(cvss_valid_prf))
        # print("Average CVSS PRF: " + str(temp_cvss_avg_prf))
        # print("CVSS PRF Seperator: " + str(cvss_seper))
        # print("Modified Average CVSS PRF: " + str(cvss_avg_prf))

    with open(tempoutfile, 'r') as csvfiletemp:
        csvreadertemp = csv.reader(csvfiletemp)
        fieldstemp = next(csvreadertemp)
        for rowtemp in csvreadertemp:
            if verbose_mode == 1:
                print(
                    "\nGenerating Remediation Priority for Row[" + str(row_count) + "]")
                time.sleep(0.3)
            if float(rowtemp[9]) >= artas_seper:
                dfnew.loc[row_count, "Remediation Priority"] = "Do Now"
                do_now_count = do_now_count + 1
            elif float(rowtemp[9]) >= artas_avg_prf:
                dfnew.loc[row_count, "Remediation Priority"] = "Do Next"
                do_next_count = do_next_count + 1
            else:
                dfnew.loc[row_count, "Remediation Priority"] = "Do Later"
                do_later_count = do_later_count + 1
            dfnew.loc[row_count, "Month Reported"] = month
            dfnew.loc[row_count, "Report Status"] = "Open"

            # if float(rowtemp[14]) >= cvss_seper:
            #     dfnew.loc[row_count, "CVSS Remediation Priority"] = "Do Now"
            # elif float(rowtemp[14]) >= cvss_avg_prf:
            #     dfnew.loc[row_count, "CVSS Remediation Priority"] = "Do Next"
            # else:
            #     dfnew.loc[row_count, "CVSS Remediation Priority"] = "Do Later"

            row_count = row_count + 1

    dfnew.to_csv(newoutfile, index=False)
    if verbose_mode == 1:
        print("\n\nRemoving temporary files")
        colorCyan("Copying to Master Report Record")

    with contextlib.suppress(FileNotFoundError):
        os.remove(tempoutfile)
        os.remove(procfile)
        os.remove(scanfile)

    os.rename(newoutfile, newoutfile.replace('.csv', '', 1))
    str_outfile = newoutfile
    str_outfile = str_outfile.replace(".csv", "", 1)

    if verbose_mode == 1:
        print(" ")
        colorCyan("\n[OPERATION COMPLETE] - REMEDIATION PRIORITIZATION")

    temp_throwaway = 0

    dftemp = pd.read_csv(str_outfile)
    with open(master_report_record, 'a') as fout:
        dftemp.to_csv(fout, header=False, index=False)
    # dfmrr = pd.read_csv(master_report_record)
    # dfmmr = pd.concat([dftemp, dfmrr], ignore_index = True)
    # dfmrr.reset_index()

    # with open(str_outfile, 'r') as f1:
    #     if temp_throwaway > 0:
    #         current_csv_file = f1.read()
    #     temp_throwaway = temp_throwaway + 1

    # with open(master_report_record, 'a') as f2:
    #     f2.write('\n')
    #     f2.write(current_csv_file)

    print("\n\n\n")
    colorGreen(bordered("          Prioritized Report - '" +
               str_outfile + "' is ready.          "))
    print("\n")
    colorGreen(bordered("          Master Report Record - '" +
               str(master_report_record) + "' updated successfully.          "))
    print(" ")


def color_information():
    colorCyan("The verbose colors represent the following:")
    colorCyan("[CYAN]       - Current executed operation is displayed in cyan.")
    colorGreen(
        "[GREEN]      - Input/Output operations are displayed in green.")
    print("[WHITE]      - Informations are displayed in white.")
    colorLightPurple("[PURPLE]     - Phases/Stages are displayed in purple.")
    colorOrange("[ORANGE]     - Warnings and alerts are displayed in orange.")
    colorRed(
        "[RED]        - Errors and other critical failures are displayed in red.")
    print("\n\n")


desc = '''
┌─────────────────────── [ ARTAS ] ───────────────────────┐
│          Automated Remediation TrAcking System          │
└─────────────────────────────────────────────────────────┘
Converts NETVAR (Network Vulnerability Assessment Report) to ARTAS Report.

// For Internal Use Only. NO DISTRIBUTION ALLOWED. //

┌──────────────────────────────────────────────── [ WARNING ] ───────────────────────────────────────────────┐
|            TO AVOID HIGH CONSUMPTION OF TIME, RESOURCES AND POSSIBLY CRASHING THE APPLICATION,             |
|              IT IS HIGLY RECOMMENDED TO SPECIFY THE STARTING AND ENDING PAGES FOR EXTRACTION.              |
└────────────────────────────────────────────────────────────────────────────────────────────────────────────┘


Below is the list of all available optional arguments and their purpose.
'''

opening_text = '''
┌─────────────────────── [ ARTAS ] ───────────────────────┐
│          Automated Remediation TrAcking System          │
└─────────────────────────────────────────────────────────┘
'''
print("\n")
colorGreen(opening_text)
print("\n")


with contextlib.suppress(FileNotFoundError):
    os.remove(procfile)
    os.remove(scanfile)

parser = argparse.ArgumentParser(
    prog="ARTAS", description=desc, formatter_class=argparse.RawDescriptionHelpFormatter)
parser.add_argument(
    "--manual", help="specifies Manaul input operation. Recommended to use along with '--verbose'.", action='store_true')
parser.add_argument(
    "--calculateprf", help="calculates Prioritized Risk Factor for the '.csv' file specified by the '--file' parameter.", action='store_true')
parser.add_argument("-s", "--start", type=int,
                    help="specifies starting page for Extraction. Defaults to beginning of the file.")
parser.add_argument("-e", "--end", type=int,
                    help="specifies ending page for Extraction. Defaults to end of the file.")
parser.add_argument(
    "-f", "--file", help="specifies the name of the input report file. Defaults to 'report.pdf'.")
parser.add_argument(
    "--vendor", help="specifies the report vendor as Deloitte (d) or WeSecureApp (w).")
parser.add_argument(
    "--name", help="specifies the project name. Defaults to the filename.")
parser.add_argument(
    "--verbose", help="provides detailed information of the operation running in the background.", action='store_true')
parser.add_argument(
    "--updatevdb", help="updates the Vulnerability Databases to reflect new cves.", action='store_true')
parser.add_argument('--version', action='version',
                    version='%(prog)s Beta-1.7.20-PreRelease')
args = parser.parse_args()

if len(sys.argv) == 1:
    colorRed(parser.print_help(sys.stderr))
    sys.exit(1)

if args.verbose:
    verbose_mode = 1
    color_information()
else:
    verbose_mode = 0

op_type = 0

code = 0

year = datetime.now().strftime('%Y')
year = int(year) + 1

if args.updatevdb:
    colorGreen("Updating Database Files")
    for each_year in range(2002, year):
        link = ('https://nvd.nist.gov/feeds/json/cve/1.1/nvdcve-1.1-' +
                str(each_year) + '.json.zip')
        if verbose_mode == 1:
            colorGreen("\nDownloading Database for the year: " + str(each_year))
            colorGreen("Using WGET Method for URL: " + str(link))
        try:
            wget.download(link)
        except:
            target_path = 'nvdcve-1.1-' + str(each_year) + '.json.zip'
            if verbose_mode == 1:
                colorOrange("WGET Method Failed. Trying Requests Method.")
                print("Target Path: ", target_path)
            r = requests.get(link)
            with open(target_path, 'wb') as f:
                f.write(r.content)
    current_directory = os.listdir('.')
    if verbose_mode == 1:
        print("\nCurrent Directory contains: ", current_directory)
    for each_file in current_directory:
        if each_file.endswith('.zip'):
            if verbose_mode == 1:
                print("ZIP File Found: ", str(each_file))
            data_zip = zipfile.ZipFile(each_file, 'r')
            if verbose_mode == 1:
                print("Extracting ZIP File to 'Data Feeds\'")
            data_zip.extractall(path='Data Feeds/')
            if verbose_mode == 1:
                print("Extraction Complete.")
            data_zip.close()
    current_directory = os.listdir('.')
    if verbose_mode == 1:
        print("\nPurging and Removing un-necessary files.")
    for each_file in current_directory:
        if each_file.endswith('.zip'):
            if verbose_mode == 1:
                print("Removing: ", str(each_file))
            os.remove(each_file)
    if verbose_mode == 1:
        print("\n\nFiles removed successfully.")
        print("Current directory contains: ", os.listdir('.'))
        print("Extracted directory contains: ", os.listdir('Data Feeds/'))
    colorGreen("\n\nDatabase Files have been updated successfully.")
    exit()


if not args.calculateprf:

    if not args.manual:

        if args.start:
            beginning = args.start

        if args.end:
            end1 = args.end

        if args.vendor:
            if args.vendor == 'd' or args.vendor == 'D':
                report_vendor = 'd'
            elif args.vendor == 'w' or args.vendor == 'W':
                report_vendor = 'w'
            else:
                colorRed(
                    "\n[ERROR] - Incorrect vendor specified.")
                exit()

        if args.file:
            if verbose_mode == 1:
                print("Verifying file location and status")
            if os.path.isfile(args.file):
                if verbose_mode == 1:
                    print("File found.")
                PDF_file = str(args.file)
            else:
                colorRed(
                    "\n[ERROR] - Unable to locate the file. Please verify the path.")
                exit()
        if verbose_mode == 1:
            print("Verifying Project Name")
        if args.name:
            project_name = str(args.name)
        else:
            project_name = str(PRF_file)
        if verbose_mode == 1:
            print("Project Name: ", project_name)
    else:
        if verbose_mode == 1:
            colorCyan("[INITIALIZING]")

        op_type = input("[E]xtraction or [P]rioritization?: ")

        if op_type == 'e' or op_type == 'E':

            in_file = input("PDF File to process: ")
            beginning = int(input("Starting Page: "))

            end1 = int(input("Ending Page: "))

            report_vendor = input(
                "Report Vendor ([D]elloite or [W]eSecureApp): ")
            report_vendor = report_vendor.lower()

            if in_file:
                if verbose_mode == 1:
                    print("Verifying file location and status")
                if os.path.isfile(in_file):
                    if verbose_mode == 1:
                        print("File found")
                    PDF_file = in_file
                else:
                    colorRed(
                        "\n[ERROR] - Unable to locate the file. Please verify the path.")
                    exit()
            project_name = str(input("Enter the Project Name: "))
        elif op_type == 'p' or op_type == 'P':
            PRF_file = input("Enter the '.csv' file: ")
            print("Checking if file exists in the location")
            if os.path.isfile(PRF_file):
                if verbose_mode == 1:
                    print("File found")
                    colorGreen("Filename: " + str(PRF_file))
                    colorCyan("[INITIALIZATION COMPLETE]")
                    colorLightPurple(
                        "STAGE [3] COMPLETE. BEGINNING STAGE [4] IN 3 SECONDS. ")
                newoutfile = 'Prioritized-%s.csv' % PRF_file
                time.sleep(3)
                prioritization()
                exit()
            else:
                colorRed(
                    "\n[ERROR] - Unable to locate the file. Please verify the path.")
                exit()

    if (beginning < 0) or (beginning > end1):
        if args.start:
            if verbose_mode == 1:
                print("Checking if the starting page is valid")
            colorOrange(
                "[WARNING] - Starting Page for Extraction appears to be invalid.")
            cont = input(
                "Do you want to modify the starting page (Y) or use defaults (N): ")
            if cont == 'Y' or cont == 'y':
                beginning = int(input("Enter your starting page: "))
                if beginning > end1:
                    colorOrange(
                        "[WARNING] - Starting Page for Extraction appears to be invalid. Defaulting to 0.")
                    beginning = 0
            else:
                beginning = 0
            if verbose_mode == 1:
                print("Starting page is set to Page: " + str(beginning))
        else:
            beginning = 0
            if verbose_mode == 1:
                print("Starting page is set to Page: " + str(beginning))

    if (end1 < beginning) or (end1 > 500):
        if args.start:
            if verbose_mode == 1:
                print("Checking if the ending page is valid")
            colorOrange(
                "[WARNING] - Ending Page for Extraction appears to be invalid.")
            cont = input(
                "Do you want to modify the ending page (Y) or use defaults (N): ")
            if cont == 'Y' or cont == 'y':
                end1 = int(input("Enter your starting page: "))
                if beginning > end1:
                    colorOrange(
                        "[WARNING] - Ending Page for Extraction appears to be invalid. Defaulting to end of file.")
                    end1 = 42069
            else:
                end1 = 42069
            if verbose_mode == 1:
                print("Ending Page is set to Page: " + str(end1))
        else:
            end1 = 42069
            if verbose_mode == 1:
                print("Ending Page is set to Page: " + str(end1))

    colorCyan("\n[INITIALIZING] Validating Input Parameters\n")
    if report_vendor == 'd' or report_vendor == 'D':
        print("Report Vendor: Deloitte")
    elif report_vendor == 'w' or report_vendor == 'W':
        print("Report Vendor: WeSecureApp")
    print("Filename: " + str(PDF_file))
    print("Starting Page: " + str(beginning))
    print("Ending Page: " + str(end1))
    print("Report/Project Name: " + str(project_name))
    time.sleep(0.5)
    colorCyan("\n[INITIALIZATION COMPLETE]\n\n")
    if verbose_mode == 1:
        colorLightPurple(
            "STAGE [0] COMPLETE. BEGINNING STAGE [1] IN 3 SECONDS. ")
    outfile = 'CRF-%s-' % str(PDF_file) + current_time + '.csv'
    time.sleep(3)
    initialization()

if args.file:
    colorCyan("[INITIALIZING] Validating Input Parameters")
    if verbose_mode == 1:
        print("Checking if file exists in the location")
    if os.path.isfile(args.file):
        PRF_file = str(args.file)
        if verbose_mode == 1:
            print("File found")
            colorGreen("Filename: " + str(PRF_file))
            colorCyan("[INITIALIZATION COMPLETE]")
            colorLightPurple(
                "STAGE [3] COMPLETE. BEGINNING STAGE [4] IN 3 SECONDS. ")
        newoutfile = 'Prioritized-%s.csv' % PRF_file
        time.sleep(3)
        prioritization()
    else:
        colorRed(
            "\n[ERROR] - Unable to locate the file. Please verify the path.")
        exit()


#####################################################################################################################    ARCHIVE     ######################################################################################################################

# https://stackabuse.com/executing-shell-commands-with-python

# def change_permissions(args, filename):
#     cmd = 'chmod'
#     ls = 'ls'
#     data = subprocess.Popen([ls, '-l', filename], stdout=subprocess.PIPE)
#     output = str(data.communicate())
#     if verbose_mode == 1:
#         print('File permissions before chmod % s: ' % (args))
#         print(output)

#     temp = subprocess.Popen([cmd, args, filename], stdout=subprocess.PIPE)

#     data = subprocess.Popen([ls, '-l', filename], stdout=subprocess.PIPE)

#     output = str(data.communicate())
#     if verbose_mode == 1:
#         print('File permissions after chmod % s: ' % (args))
#         print(output)


# object►CVE_Items►0►cve►CVE_data_meta►ID

# object►CVE_Items►0►impact►baseMetricV3►cvssV3►vectorString
# object►CVE_Items►0►impact►baseMetricV3►cvssV3►baseScore

# object►CVE_Items►0►impact►baseMetricV2►cvssV2►vectorString
# object►CVE_Items►0►impact►baseMetricV2►cvssV2►baseScore

# object►CVE_Items►0►cve►references►reference_data►0►tags

# ls
# touch testfile1.json
# touch app.py
# mkdir "Data Feeds"
# ls
# mv testfile1.json Data\ Feeds
# cd Data\ Feeds
# ls

# import os
# import json
# cwd = "Data Feeds"
# os.chdir(cwd)
# try:
#     cveapirespdata = json.load("testfile1.json")
#     print("Success")
# except:
#     print("Something wrong.")
