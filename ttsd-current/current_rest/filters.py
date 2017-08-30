import django_filters

from current_rest import models


class DepositFilter(django_filters.FilterSet):
    updated_time = django_filters.DateFromToRangeFilter()

    class Meta:
        model = models.CurrentDeposit
        fields = ('current_account__mobile', 'status', 'source', 'updated_time')


