# DataViewer
Application for tracking statistics in the Yandex Direct contextual advertising system and the Yandex Metrika analytics system.

The following functionality has been implemented:
1.Daily data update for the past day
2.Selection of a long data update period
3.Accounting for statistics on several Yandex Direct accounts and Metrika counters
4.Creation of separate projects for different accounts and counters
5.Saving tokens to obtain statistics for each account
6.Registration of individual users and authentication
7.Calculation of basic measures of advertising effectiveness - CPC, CPM, CTR, CR

Data is downloaded using the Yandex API:
https://yandex.ru/dev/direct/
https://yandex.ru/dev/metrika/

# Tech-stack
- XML
- Single Activity
- MVVM
- Feature Modules Architecture
- LiveData
- Flow
- Coroutines
- Dagger
- Fragment Manager 
- Room
- Retrofit
